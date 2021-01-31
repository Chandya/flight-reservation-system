/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.FlightDisabledException;
import util.exception.FlightPlanExistsException;
import util.exception.FlightPlanNotFoundException;
import util.exception.FlightPlanUpdateException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.FlightUpdateException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */
@Stateless
public class FlightSchedulePlanSessionBean implements FlightSchedulePlanSessionBeanRemote, FlightSchedulePlanSessionBeanLocal {

    @EJB(name = "FlightScheduleSessionBeanLocal")
    private FlightScheduleSessionBeanLocal flightScheduleSessionBeanLocal;

    @PersistenceContext(unitName = "FlightReservation-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public FlightSchedulePlanSessionBean(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan) throws UnknownPersistenceException, FlightPlanExistsException, InputDataValidationException, FlightDisabledException {
       Set<ConstraintViolation<FlightSchedulePlan>>constraintViolations = validator.validate(newFlightSchedulePlan);
        
        if(constraintViolations.isEmpty()){
            try{
                em.persist(newFlightSchedulePlan);
                em.flush();
                
                return newFlightSchedulePlan;
            }
            catch(PersistenceException ex){
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new FlightPlanExistsException();
                    }
                    else
                    {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        
       
            
            //check if there is overlap
        } else if (newFlightSchedulePlan.getFlight().isIsDisabled()) {
            throw new FlightDisabledException("This flight is disabled");
            //check if flight is disabled
            
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
                
    }
    
    
    @Override
    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans() {
        //SELECT a FROM Author a ORDER BY a.lastName ASC, a.firstName DESC
        Query query = em.createQuery("SELECT fp FROM FlightSchedulePlan fp ORDER BY fp.flightNum ASC fp.firstTime DESC");
        return query.getResultList();
    }
    
    public FlightSchedulePlan retrieveFlighSchedulePlanById(Long fpId) throws FlightSchedulePlanNotFoundException {
        FlightSchedulePlan fp = em.find(FlightSchedulePlan.class, fpId);
        if(fp != null)
        {
            return fp;
        }
        else
        {
            throw new FlightSchedulePlanNotFoundException("FlightSchedulePlanId " + fpId + " does not exist!");
        }
    }
    
    @Override
    public void updateFlightSchedulePlan(FlightSchedulePlan newFp, Long flightId) throws FlightSchedulePlanNotFoundException, FlightPlanUpdateException, InputDataValidationException, FlightDisabledException {
        if(newFp != null && newFp.getFlightSchedulePlanId() != null){
            Set<ConstraintViolation<FlightSchedulePlan>>constraintViolations = validator.validate(newFp);
            if(constraintViolations.isEmpty()){
                FlightSchedulePlan flightToUpdate = retrieveFlighSchedulePlanById(flightId);
                if(flightToUpdate.getFlightNum().equals(newFp.getFlightNum())){
                    if (!(newFp.getFlight().isIsDisabled())) {
                        flightToUpdate.setFlight(newFp.getFlight());
                        flightToUpdate.setScheduleType(newFp.getScheduleType());
                        flightToUpdate.setSchedules(newFp.getSchedules());
                    } else {
                        throw new FlightDisabledException("This flight is disabled");
                    }
                }else{
                    throw new FlightPlanUpdateException("Flight Number of flight record to be updated does not match the existing record");
                }
            }else{
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));       
            }
        }else{
            throw new FlightSchedulePlanNotFoundException("FlightPlan ID not provided for flight plan to be updated");        
        }
    }
    
    @Override
    public void deleteFlightSchedulePlan(Long fpId) throws FlightSchedulePlanNotFoundException {
        FlightSchedulePlan flightToRemove = retrieveFlighSchedulePlanById(fpId);
        if (flightToRemove != null && flightToRemove.getFlightSchedulePlanId() != null) {
            if (flightToRemove.isIsUsed() == false) {
                flightToRemove.setFlight(null);
                em.remove(flightToRemove);
            } else {
                flightToRemove.setIsDisabled(true);
            }
        } else {
            throw new FlightSchedulePlanNotFoundException("FlightSchedulePlan ID not provided for flight schedule plan to be deleted");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<FlightSchedulePlan>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

 
}
