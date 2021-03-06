/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Aircraft;
import javax.ejb.Local;
import util.exception.AircraftExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */
@Local
public interface AircraftSessionBeanLocal {

    public Long createNewAircraft(Aircraft newAircraft) throws AircraftExistException, UnknownPersistenceException, InputDataValidationException;
    
}
