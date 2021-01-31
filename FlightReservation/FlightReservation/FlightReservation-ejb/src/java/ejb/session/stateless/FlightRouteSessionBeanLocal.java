/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Route;
import java.util.List;
import javax.ejb.Local;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.InvalidAirportException;
import util.exception.RouteIsBeingUsedException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author chandya
 */
@Local
public interface FlightRouteSessionBeanLocal {
    
    public Route createNewFlightRoute(Route fr) throws FlightRouteExistException, UnknownPersistenceException;
    public void persist(Object object);

    public List<Route> retrieveAllFlightRoutes();

    public Route retrieveFlightRoutebyId(Long frId) throws FlightRouteNotFoundException;

    public void deleteRoute(Long frId) throws FlightRouteNotFoundException, RouteIsBeingUsedException;

}
