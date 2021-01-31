/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import entity.Airport;
import entity.Route;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AircraftConfigurationExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.DeleteRouteException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.InvalidAirportException;
import util.exception.RouteIsBeingUsedException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author chandya
 */
@Stateless
public class FlightRouteSessionBean implements FlightRouteSessionBeanRemote, FlightRouteSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservation-ejbPU")
    private EntityManager em;

    @Override
    public void persist(Object object) {
        em.persist(object);
    }

    public FlightRouteSessionBean() {
    }
    
    @Override
    public Route createNewFlightRoute(Route fr) throws FlightRouteExistException, UnknownPersistenceException {
        
        try
        {
            em.persist(fr);
            em.flush();

            return fr;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new FlightRouteExistException();
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
    
    @Override
    public List<Route> retrieveAllFlightRoutes() {
        Query query;
        query = em.createQuery("SELECT fr FROM Route fr ORDER BY fr.originName ASC");
        List<Route> allRoutes = query.getResultList();
        return allRoutes;
    }
    
    @Override
    public Route retrieveFlightRoutebyId(Long frId) throws FlightRouteNotFoundException
    {
        Route fr = em.find(Route.class, frId);
        
        if(fr != null)
        {
            return fr;
        }
        else
        {
            throw new FlightRouteNotFoundException("FlightRouteId " + frId + " does not exist!");
        }               
    }
    
     @Override
     public void deleteRoute(Long routeId) throws FlightRouteNotFoundException, RouteIsBeingUsedException
    {   
        Route routeToRemove = retrieveFlightRoutebyId(routeId);
        if (routeToRemove != null) {
            if (routeToRemove.getFlights().isEmpty()) {
                em.remove(routeToRemove);
            } else {
                routeToRemove.setIsDisabled(true);
                throw new RouteIsBeingUsedException("Route attempted to delete is being used");
            }
        } else {
            throw new FlightRouteNotFoundException();
        }
        
        
        
        
    }
   
}
