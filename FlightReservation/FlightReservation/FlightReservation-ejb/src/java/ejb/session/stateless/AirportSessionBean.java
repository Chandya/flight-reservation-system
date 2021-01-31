/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AirportExistException;
import util.exception.AirportNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author chandya
 */
@Stateless
public class AirportSessionBean implements AirportSessionBeanLocal, AirportSessionBeanRemote {

    @PersistenceContext(unitName = "FlightReservation-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public AirportSessionBean(){
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewAirport(Airport newAirport) throws AirportExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Airport>>constraintViolations = validator.validate(newAirport);
        
        if(constraintViolations.isEmpty()){
            try{
                em.persist(newAirport);
                em.flush();
                
                return newAirport.getAirportId();
            }
            catch(PersistenceException ex){
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new AirportExistException();
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
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public Airport retrieveAirportByAirportId(Long airportId) throws AirportNotFoundException {
        Airport airportToRetrieve = em.find(Airport.class, airportId);
        if(airportToRetrieve != null){
            return airportToRetrieve;
        }else{
            throw new AirportNotFoundException("Airport ID " + airportId + " does not exist!");
        }
    }
    
    @Override
    public Airport retrieveAirportByIATA(String IATAcode) throws AirportNotFoundException {
        Query query = em.createQuery("SELECT a FROM Airport a WHERE a.IATACode = :inCode");
        query.setParameter("inCode", IATAcode);
        
        try {
            return (Airport)query.getSingleResult();
        }catch(NoResultException | NonUniqueResultException ex) {
            throw new AirportNotFoundException("Airport " + IATAcode + " does not exist!");
        }
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Airport>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}


