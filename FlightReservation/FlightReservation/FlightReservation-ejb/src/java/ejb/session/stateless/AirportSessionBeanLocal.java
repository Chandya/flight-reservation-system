/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Airport;
import javax.ejb.Local;
import util.exception.AirportExistException;
import util.exception.AirportNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author chandya
 */
@Local
public interface AirportSessionBeanLocal {

    public Long createNewAirport(Airport newAirport) throws AirportExistException, UnknownPersistenceException, InputDataValidationException;

    public Airport retrieveAirportByAirportId(Long airportId) throws AirportNotFoundException;

    public Airport retrieveAirportByIATA(String IATAcode) throws AirportNotFoundException;
    
}
