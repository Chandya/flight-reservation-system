/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entity.Flight;
import entity.Route;
import entity.Schedule;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteFlightException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightUpdateException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.RouteDisabledException;

/**
 *
 * @author Jarryl Yeo
 */
@Stateless
public class FlightSessionBean implements FlightSessionBeanRemote, FlightSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservation-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public void persist(Object object) {
        em.persist(object);
    }

    public FlightSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Flight createNewFlight(Flight newFlight) throws FlightExistException, GeneralException,RouteDisabledException {
        try {
            em.persist(newFlight);
            em.flush();

        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getCause() != null && ex.getCause().getCause().getClass()
                    .getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                throw new FlightExistException("Flight with the same name exists!");
            } else {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        
        if (newFlight.getRoute().isIsDisabled()) {
            throw new RouteDisabledException("This route is disabled");
            
        } else {
            return newFlight;
        }
        
        
    }

    @Override
    public List<Flight> retrieveAllFlight() {
        Query query = em.createQuery("SELECT f FROM Flight f ORDER BY f.flightNum ASC");
        return query.getResultList();
    }

    @Override
    public Flight retrieveFlightByName(String name) throws FlightNotFoundException, FlightUpdateException {
        Query query = em.createQuery("SELECT f FROM Flight f WHERE f.flightNum LIKE :inName");
        query.setParameter("inName", name);
        //Flight flight = new Flight();
        try {
            Flight flight = (Flight) query.getSingleResult();
            if (flight.isIsDisabled()) {
                throw new FlightUpdateException("This flight is disabled");
            }
            return flight;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new FlightNotFoundException("This flight is not in the database.");
        }   
    }

    @Override
    public Flight retrieveFlightById(Long flightId) throws FlightNotFoundException {
        Flight flight = em.find(Flight.class, flightId);
        if (flight != null) {
            return flight;
        } else {
            throw new FlightNotFoundException("Flight does not exist" + flightId);
        }
    }

    @Override
    public void updateFlight(Flight flight)
            throws FlightNotFoundException, FlightUpdateException, InputDataValidationException {
        if (flight != null && flight.getFlightId() != null) {
            Set<ConstraintViolation<Flight>> constraintViolations = validator.validate(flight);
            if (constraintViolations.isEmpty()) {
                Flight flightToUpdate = retrieveFlightByName(flight.getFlightNum());
                if (flightToUpdate.getFlightNum().equals(flight.getFlightNum())) {
                    if (!(flight.getRoute().isIsDisabled())) {
                        flightToUpdate.setAircraftConfiguration(flight.getAircraftConfiguration());
                        flightToUpdate.setRoute(flight.getRoute());
                    } else {
                        throw new FlightUpdateException("This record is disabled");
                    }

                } else {
                    throw new FlightNotFoundException(
                            "Flight Number of flight record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new FlightNotFoundException("Flight ID not provided for flight to be updated");
        }
    }

    @Override
    public void deleteFlight(Long flightId) throws FlightNotFoundException {
        Flight flightToRemove = retrieveFlightById(flightId);
        if (flightToRemove != null && flightToRemove.getFlightId() != null) {
            if (flightToRemove.getFlightSchedulePlans().isEmpty()) {
                flightToRemove.setAircraftConfiguration(null);
                 flightToRemove.setRoute(null);
                em.remove(flightToRemove);
            } else {
                flightToRemove.setIsDisabled(true);
            }
        } else {
            throw new FlightNotFoundException("Flight ID not provided for flight to be deleted");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Flight>> constraintViolations) {
        String msg = "Input data validation error!:";
        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "n't" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + ";"
                    + constraintViolation.getMessage();
        }

        return msg;
    }

}
// Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method")
