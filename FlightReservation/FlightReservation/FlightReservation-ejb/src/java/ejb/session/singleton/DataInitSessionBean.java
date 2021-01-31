/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AircraftSessionBeanLocal;
import ejb.session.stateless.AirportSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Aircraft;
import entity.Airport;
import entity.Employee;
import entity.Partner;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.PersistenceContext;
import util.enumeration.UserRole;
import util.exception.AircraftExistException;
import util.exception.AirportExistException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.PartnerNotFoundException;
import util.exception.PartnerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {
   
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    @EJB 
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    @EJB
    private AirportSessionBeanLocal airportSessionBeanLocal;
    @EJB
    private AircraftSessionBeanLocal aircraftSessionBeanLocal;
    
    
    public DataInitSessionBean() {
        
    }
    
    @PostConstruct
    public void postConstruct() {
       
        
       try {
           employeeSessionBeanLocal.retrieveEmployeeByUsername("routeplanner");
       } catch (EmployeeNotFoundException ex) {
            initialiseData();
       }
        
    }
    
    private void initialiseData() {
        
        try {
            employeeSessionBeanLocal.createNewEmployee(new Employee("Fleet Manager", "fleetmanager", "password", UserRole.FLEETMANAGER));

            employeeSessionBeanLocal.createNewEmployee(new Employee("Route Planner", "routeplanner", "password", UserRole.ROUTEPLANNER));

            employeeSessionBeanLocal.createNewEmployee(new Employee("Schedule Manager", "schedulemanager", "password", UserRole.SCHEDULEMANAGER));

            employeeSessionBeanLocal.createNewEmployee(new Employee("Sales Manager", "salesmanager", "password", UserRole.SALESMANAGER));
            
            partnerSessionBeanLocal.createNewPartner(new Partner("Holiday.com", "holidaydotcom", "password"));
            
            airportSessionBeanLocal.createNewAirport(new Airport("Changi", "SIN", "Singapore", "Singapore", "Singapore", "+8"));
            
            airportSessionBeanLocal.createNewAirport(new Airport("Hong Kong", "HKG", "Chek Lap Kok", "Hong Kong", "China", "+8"));
            
            airportSessionBeanLocal.createNewAirport(new Airport("Taoyuan", "TPE", "Taoyuan", "Taipei", "Taiwan R.O.C.", "+8"));
            
            airportSessionBeanLocal.createNewAirport(new Airport("Narita", "NRT", "Narita", "Chiba", "Japan", "+9"));
            
            airportSessionBeanLocal.createNewAirport(new Airport("Sydney", "SYD", "Sydney", "New South Wales", "Australia", "+11"));
            
            aircraftSessionBeanLocal.createNewAircraft(new Aircraft("Boeing 747", 400));
            
            aircraftSessionBeanLocal.createNewAircraft(new Aircraft("Boeing 737", 200));
            
        } catch (EmployeeUsernameExistException|UnknownPersistenceException|InputDataValidationException|PartnerUsernameExistException | AirportExistException | AircraftExistException ex) {
            ex.printStackTrace();
        }
       
    }
}
