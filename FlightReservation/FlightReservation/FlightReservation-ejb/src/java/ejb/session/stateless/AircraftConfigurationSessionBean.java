/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AircraftConfiguration;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.AircraftCapacityInvalidException;
import util.exception.AircraftConfigurationExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author chandya
 */
@Stateless
public class AircraftConfigurationSessionBean implements AircraftConfigurationSessionBeanRemote, AircraftConfigurationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservation-ejbPU")
    private EntityManager em;
    
    @Override
    public void persist(Object object) {
        em.persist(object);
    }

    public AircraftConfigurationSessionBean() {
    }
    
    
    
    @Override
    public AircraftConfiguration createNewAircraftConfiguration(AircraftConfiguration ac) throws AircraftConfigurationExistException, UnknownPersistenceException, AircraftCapacityInvalidException {
        int maximumCap = 0;
        if (ac.getAircraftConfigurationName().substring(0,10).equals("Boeing 737")) {
            maximumCap = 200;
        } else {
            maximumCap = 400;
        }
        
        if (ac.getMaximumCapacity() > maximumCap) {
            throw new AircraftCapacityInvalidException("This configuration's capacity exceeds its aircraft's capacity");
        }
        
        try
        {
            em.persist(ac);
            em.flush();

            return ac;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new AircraftConfigurationExistException();
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
    public List<AircraftConfiguration> retrieveAllAircraftConfiguration() {
        Query query;
        query = em.createQuery("SELECT ac FROM AircraftConfiguration ac ORDER BY ac.aircraftConfigurationName ASC");
        List<AircraftConfiguration> acList = query.getResultList();
        return acList;
    }
    
    @Override
    public AircraftConfiguration retrieveAircraftConfigurationbyName(String aircraftConfigurationName) throws AircraftConfigurationNotFoundException
    {
        
        AircraftConfiguration aircraftConfiguration = (AircraftConfiguration) em.createQuery(
                        "SELECT ac FROM AircraftConfiguration ac WHERE ac.aircraftConfigurationName LIKE :inName")
                        .setParameter("inName", aircraftConfigurationName)
                        .getSingleResult();
        
        if(aircraftConfiguration != null) {
            return aircraftConfiguration;
     
        } else {
        
            throw new AircraftConfigurationNotFoundException("AircraftConfiguration does not exist!");
        }
    }      
    
    @Override
    public AircraftConfiguration retrieveAircraftConfigurationbyId(Long acId) throws AircraftConfigurationNotFoundException {
         AircraftConfiguration ac = em.find(AircraftConfiguration.class, acId);
         if(ac != null)
        {
            return ac;
        }
        else
        {
            throw new AircraftConfigurationNotFoundException("AircraftConfiguration does not exist!");
        }  
     }
}

  
    

