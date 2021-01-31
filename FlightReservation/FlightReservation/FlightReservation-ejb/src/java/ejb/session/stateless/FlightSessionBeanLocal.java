/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Flight;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface FlightSessionBeanLocal {
    public Flight createNewFlight(Flight newFlight) throws FlightExistException, GeneralException, RouteDisabledException;
    public List<Flight> retrieveAllFlight();
    public Flight retrieveFlightById(Long flightId) throws FlightNotFoundException;
    public Flight retrieveFlightByName(String name) throws FlightNotFoundException, FlightUpdateException;
    public void updateFlight(Flight flight) throws FlightNotFoundException, FlightUpdateException, InputDataValidationException;

    public void deleteFlight(Long flightId) throws FlightNotFoundException;

    
}
