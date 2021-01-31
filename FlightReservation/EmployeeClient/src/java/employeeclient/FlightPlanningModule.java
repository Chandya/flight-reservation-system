/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeeclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.AircraftSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.Aircraft;
import entity.AircraftConfiguration;
import entity.Airport;
import entity.CabinClass;
import entity.Employee;
import entity.Route;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.UserRole;
import util.exception.AircraftCapacityInvalidException;
import util.exception.AircraftConfigurationExistException;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.AirportNotFoundException;
import util.exception.FlightRouteExistException;
import util.exception.FlightRouteNotFoundException;
import util.exception.InvalidAirportException;
import util.exception.InvalidUserRoleException;
import util.exception.MaximumCapacityExceedException;
import util.exception.RouteIsBeingUsedException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */
public class FlightPlanningModule {
    
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private AircraftSessionBeanRemote aircraftSessionBeanRemote;
    private AirportSessionBeanRemote  airportSessionBeanRemote;
    private Employee currentEmployee;
    
    public FlightPlanningModule(){
    }
    
    public FlightPlanningModule(FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, AircraftSessionBeanRemote aircraftSessionBeanRemote, Employee currentEmployee,AirportSessionBeanRemote airportSessionBeanRemote){
        this();
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.aircraftSessionBeanRemote = aircraftSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuFlightPlanningModule() throws InvalidUserRoleException{
        
        if(currentEmployee.getUserRole() != UserRole.FLEETMANAGER){
            throw new InvalidUserRoleException("You don't have Fleet Manager or Route Planner to access the flight planning module");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0; 
        while(true){
            System.out.println("***Employee Flight System :: Flight Operation Module ***\n");
            System.out.println("1: Create Aircraft Configuration");
            System.out.println("2: View All Aircraft Configuration");
            System.out.println("3: View Aircraft Configuration Details");
            System.out.println("4: Create Flight Route");
            System.out.println("5: View All Flight Routes");
            System.out.println("6: Delete Flight Route");
            System.out.println("7: Back\n");
            response = 0;
            
            while(response < 1 || response > 6){
                System.out.println("> ");
                response = scanner.nextInt();
                
                if(response == 1){
                    doCreateAircraftConfiguration();
                }else if(response == 2){
                    doViewAllAircraftConfigurations();
                    //break;
                }else if(response == 3) {
                    doViewAircraftConfiguration();
                    //break;
                }else if(response == 4) {
                    createFlightRoute();
                    //break;
                }else if(response == 5) {
                    viewAllFlightRoutes();
                    //break;
                }else if(response == 6) {
                    deleteFlightRoute();
                    //break;
                }else if(response == 7){
                    break;
                }else{
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if(response == 7){
                break;
            }
        }
    }
    
    public void doCreateAircraftConfiguration() {
        Scanner scanner = new Scanner(System.in);
        AircraftConfiguration ac = new AircraftConfiguration();
        CabinClass cc = new CabinClass();
        System.out.println("Please enter the name of configuration>");
        String name = scanner.nextLine().trim();
        ac.setAircraftConfigurationName(name);
        System.out.println("Please enter the number of cabin classes>");
        int numCabinClass = scanner.nextInt();
        ac.setNumCabinClass(numCabinClass);
        System.out.println("Please enter the maximum capacity of this configuration>");
        int flightTotal = scanner.nextInt();
        scanner.nextLine();
        ac.setMaximumCapacity(flightTotal);
        List<CabinClass> ccList = new ArrayList<>();
        for (int i = 0; i < numCabinClass; i++) {
            System.out.println("*** Create cabin classes *** >");
            System.out.println("Please enter the type of cabin class (F,J,W or Y?)>");
            String ccType = scanner.nextLine().trim();
            cc.setType(ccType);
            System.out.println("Please enter the number of aisles>");
            int numAisles = scanner.nextInt();
            cc.setNumAisle(numAisles);
            System.out.println("Please enter the number of rows>");
            int numRows = scanner.nextInt();
            cc.setNumRow(numRows);
            System.out.println("Please enter the number of seats abreast>");
            int numSeatAbreast = scanner.nextInt();
            cc.setNumSeatsAbreast(numSeatAbreast);
            scanner.nextLine();
            System.out.println("Please enter the number of actual seat configuration of this cabin>");
            String seatConfiguration = scanner.nextLine().trim();
            cc.setActualSeatingConfiguration(seatConfiguration);
            System.out.println("Finally, enter the max capacity of this cabin class>");
            int cabinCapacity = scanner.nextInt();
            scanner.nextLine();
            cc.setMaxSeatCapacity(cabinCapacity);
            ccList.add(cc);
        }
        ac.setCabinClass(ccList);
        
     
            
        try {
            aircraftConfigurationSessionBeanRemote.createNewAircraftConfiguration(ac);
        } catch (AircraftCapacityInvalidException ex) {
            System.out.println(ex.getMessage());
        } catch (AircraftConfigurationExistException ex) {
            System.out.println("This configuration already exists: " + ex.getMessage() + "!\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println("There is a persistence exception: " + ex.getMessage() + "!\n");
        }
            
        
        } 
    
    
    public void doViewAllAircraftConfigurations() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("**View All Aircraft Configuration**\n");
        
        List<AircraftConfiguration> acConfigurations = aircraftConfigurationSessionBeanRemote.retrieveAllAircraftConfiguration();
        
        for (AircraftConfiguration ac : acConfigurations) {
            System.out.printf("%30s\n",ac.getAircraftConfigurationName());
        }
        System.out.print("Press any key to continue...> ");
        sc.nextLine();
    }
        
    public void doViewAircraftConfiguration() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View Aircraft Configuration Details ***\n");
        System.out.print("Enter Name > ");
        String name = sc.nextLine().trim();
        try {
            AircraftConfiguration aircraftConfiguration = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationbyName(name);
            System.out.println(aircraftConfiguration.getAircraftConfigurationName());
        
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("Error!" + ex.getMessage() + "\n");
        }
    }
    
    public void createFlightRoute() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        System.out.println("Enter Origin Airport > ");
        String origin = sc.nextLine().trim();
        System.out.println("Enter Destination Airport > ");
        String destination = sc.nextLine().trim();
        Route fr = new Route();
        
        try {
            Airport originAP = airportSessionBeanRemote.retrieveAirportByIATA(origin);
            Airport destinationAP = airportSessionBeanRemote.retrieveAirportByIATA(destination);
            fr.setOrigin(originAP);
            fr.setDestination(destinationAP);
            System.out.println("Would you like to create an opposite route? >");
            System.out.println("1: Yes ");
            System.out.println("2: No \n");
            System.out.println("> ");
            response = sc.nextInt();
            if (response == 1){
               fr.setHasOppRoute(true);
            }
            flightRouteSessionBeanRemote.createNewFlightRoute(fr);
        } catch (AirportNotFoundException ex) {
            System.out.println("This airport does not exist: " + ex.getMessage() + "!\n");
        } catch (FlightRouteExistException ex) {
            System.out.println("This route already exists: " + ex.getMessage() + "!\n");
        } catch (UnknownPersistenceException ex) {
            System.out.println("There is a persistence exception: " + ex.getMessage() + "!\n");
        }
    }
    
    public void viewAllFlightRoutes() {
        System.out.println("*** Fleet Management System :: System Administration :: View All Flight Routes ***\n");
        List<Route> allRoutes = flightRouteSessionBeanRemote.retrieveAllFlightRoutes();
        System.out.println(allRoutes.toString());
        for (int i = 0; i < allRoutes.size(); i++) {
            Route r = (Route)allRoutes.get(i);
            if (r.isHasOppRoute() == false) {
                System.out.println(r.getRouteId() + " " + r.getOrigin().getAirportName() + " & " + r.getDestination().getAirportName() + "\n");
            } else {
                System.out.println(r.getRouteId() + " " + r.getOrigin().getAirportName() + " & " + r.getDestination().getAirportName());
                System.out.println(r.getDestination().getAirportName() + " & " + r.getOrigin().getAirportName() + "\n");
            }
        }
    }
    
    public void deleteFlightRoute() {
        Scanner scanner = new Scanner(System.in);
        viewAllFlightRoutes();
        System.out.println("Enter the routeId you would like to delete");
        Long routeId = scanner.nextLong();
      
        
        try {
            flightRouteSessionBeanRemote.deleteRoute(routeId);
        } catch (RouteIsBeingUsedException ex){
            System.out.println("This route is disabled: " + ex.getMessage() + "!\n");
        } catch (FlightRouteNotFoundException ex) {
            System.out.println("This route doesn't exist: " + ex.getMessage() + "!\n");
        }
    }
    
    
     
        
 }

