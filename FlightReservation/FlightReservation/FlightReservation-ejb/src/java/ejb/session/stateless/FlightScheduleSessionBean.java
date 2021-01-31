/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Schedule;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.ScheduleExistException;
import util.exception.ScheduleNotFoundException;
import util.exception.ScheduleUpdateException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */
@Stateless
public class FlightScheduleSessionBean implements FlightScheduleSessionBeanRemote, FlightScheduleSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservation-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public FlightScheduleSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Schedule createNewSchedule(Schedule newSchedule)
            throws ScheduleExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Schedule>> constraintViolations = validator.validate(newSchedule);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newSchedule);
                em.flush();
                return newSchedule;

            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName()
                        .equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName()
                            .equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ScheduleExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<Schedule> retrieveAllSchedule() {
        Query query = em.createQuery("SELECT s FROM Schedule s ORDER BY s.ScheduleId ASC");
        return query.getResultList();
    }

    @Override
    public Schedule retrieveScheduleByScheduleId(Long scheduleId) throws ScheduleNotFoundException {
        Schedule scheduleToFind = em.find(Schedule.class, scheduleId);
        if (scheduleToFind != null) {
            return scheduleToFind;
        } else {
            throw new ScheduleNotFoundException("Schedule ID" + scheduleId + " does not exist!");
        }
    }

    @Override
    public void updateSchedule(Schedule scheduleToUpdate)
            throws ScheduleNotFoundException, ScheduleUpdateException, InputDataValidationException {
        if (scheduleToUpdate != null && scheduleToUpdate.getScheduleId() != null) {
            Set<ConstraintViolation<Schedule>> constraintViolations = validator.validate(scheduleToUpdate);
            if (constraintViolations.isEmpty()) {
                Schedule scheduleUpdating = retrieveScheduleByScheduleId(scheduleToUpdate.getScheduleId());
                scheduleUpdating.setDepartureDate(scheduleToUpdate.getDepartureDate());
                //scheduleUpdating.setRoute(scheduleToUpdate.getRoute());
                scheduleUpdating.setEstimatedDuration(scheduleToUpdate.getEstimatedDuration());
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new ScheduleNotFoundException("Schedule ID not provided for schedule to be updated.");
        }
    }

    @Override
    public void deleteSchedule(Long scheduleId) throws ScheduleNotFoundException {
        Schedule scheduleToRemove = retrieveScheduleByScheduleId(scheduleId);
        if (scheduleToRemove != null && scheduleToRemove.getScheduleId() != null) {
            em.remove(scheduleToRemove);
        } else {
            throw new ScheduleNotFoundException("ScheduleId not provided to delete the schedule.");
        }
    }
    
    @Override
    public List<Schedule> retrieveAllScheduleBySearchResult(Date newDepartureDate, String origin, String destination)
    {
        List<Schedule> schedulesToSearch = retrieveAllSchedule();
        List<Schedule> scheduleToSendBack = new ArrayList<>();
        for(Schedule s : schedulesToSearch){
            if(s.getDepartureDate().equals(newDepartureDate) && s.getFlightSchedulePlan().getFlight().getRoute().getOrigin().getIATACode().equals(origin) && s.getFlightSchedulePlan().getFlight().getRoute().getDestination().getIATACode().equals(destination)){
                scheduleToSendBack.add(s);
            }
        }
        return scheduleToSendBack;
    }


    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Schedule>> constraintViolations) {
        String msg = "Input data validation error!:";
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "n't" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + ";"
                    + constraintViolation.getMessage();
        }
        return msg;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
