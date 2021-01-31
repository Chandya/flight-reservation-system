/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Schedule;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.ScheduleExistException;
import util.exception.ScheduleNotFoundException;
import util.exception.ScheduleUpdateException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */
@Remote
public interface FlightScheduleSessionBeanRemote {
    public List<Schedule> retrieveAllSchedule();
    public Schedule createNewSchedule(Schedule newSchedule) throws ScheduleExistException, UnknownPersistenceException, InputDataValidationException;
    public Schedule retrieveScheduleByScheduleId(Long scheduleId) throws ScheduleNotFoundException;
    public void deleteSchedule(Long scheduleId) throws ScheduleNotFoundException;
    public void updateSchedule(Schedule scheduleToUpdate) throws ScheduleNotFoundException, ScheduleUpdateException, InputDataValidationException;
    public List<Schedule> retrieveAllScheduleBySearchResult(Date newDepartureDate, String origin, String destination);
    
}
