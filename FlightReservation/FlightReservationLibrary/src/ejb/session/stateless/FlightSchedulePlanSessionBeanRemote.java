/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.FlightSchedulePlan;
import java.util.List;
import javax.ejb.Remote;
import util.exception.FlightDisabledException;
import util.exception.FlightPlanExistsException;
import util.exception.FlightPlanUpdateException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */
@Remote
public interface FlightSchedulePlanSessionBeanRemote {
    
    public FlightSchedulePlan createNewFlightSchedulePlan(FlightSchedulePlan newFlightSchedulePlan) throws UnknownPersistenceException, FlightPlanExistsException, InputDataValidationException, FlightDisabledException;

    public List<FlightSchedulePlan> retrieveAllFlightSchedulePlans();
    
    public FlightSchedulePlan retrieveFlighSchedulePlanById(Long fpId) throws FlightSchedulePlanNotFoundException;

    public void updateFlightSchedulePlan(FlightSchedulePlan newFp, Long flightId) throws FlightSchedulePlanNotFoundException, FlightPlanUpdateException, InputDataValidationException, FlightDisabledException;
    
    public void deleteFlightSchedulePlan(Long fpId) throws FlightSchedulePlanNotFoundException;
    
}
