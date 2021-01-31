/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import java.util.List;
import javax.ejb.Remote;
import util.exception.AircraftCapacityInvalidException;
import util.exception.AircraftConfigurationExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author chandya
 */
@Remote
public interface AircraftConfigurationSessionBeanRemote {
    
    public AircraftConfiguration createNewAircraftConfiguration(AircraftConfiguration ac) throws AircraftConfigurationExistException, UnknownPersistenceException, AircraftCapacityInvalidException;

    public void persist(Object object);
    
    public List<AircraftConfiguration> retrieveAllAircraftConfiguration();
    
    public AircraftConfiguration retrieveAircraftConfigurationbyId(Long acId) throws AircraftConfigurationNotFoundException;

    public AircraftConfiguration retrieveAircraftConfigurationbyName(String aircraftConfigurationName) throws AircraftConfigurationNotFoundException;
}
