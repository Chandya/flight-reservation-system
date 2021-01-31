/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeeclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import entity.AircraftConfiguration;
import entity.Employee;
import entity.Fare;
import entity.Flight;
import entity.FlightSchedulePlan;
import entity.Route;
import entity.Schedule;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import util.enumeration.UserRole;
import util.exception.AircraftConfigurationNotFoundException;
import util.exception.FlightDisabledException;
import util.exception.FlightExistException;
import util.exception.FlightNotFoundException;
import util.exception.FlightPlanExistsException;
import util.exception.FlightPlanUpdateException;
import util.exception.FlightRouteNotFoundException;
import util.exception.FlightSchedulePlanNotFoundException;
import util.exception.FlightUpdateException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.InvalidUserRoleException;
import util.exception.RouteDisabledException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */
public class FlightOperationModule {
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private AircraftSessionBeanRemote aircraftSessionBeanRemote;
    private Employee currentEmployee;
    
    public FlightOperationModule(){
    }
    
    public FlightOperationModule(FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, AircraftSessionBeanRemote aircraftSessionBeanRemote, Employee currentEmployee){
        this();
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.aircraftSessionBeanRemote = aircraftSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    public void menuFlightOperationModule() throws InvalidUserRoleException{
        if(currentEmployee.getUserRole() != UserRole.SCHEDULEMANAGER){
            throw new InvalidUserRoleException("You don't have Schedule Manager rights to access the flight planning module");
        }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            System.out.println("*** Employee Flight System :: Flight Operation ***\n");
            System.out.println("1: Create Flight");
            System.out.println("2: View All Flight");
            System.out.println("3: View Flight Details");
            System.out.println("4: Update Flight");
            System.out.println("5: Delete Flight");
            System.out.println("6: Create Flight Schedule Plan");
            System.out.println("7: View All Flight Schedule Plan");
            System.out.println("8: View Flight Schedule Plan Details");
            System.out.println("9: Update Flight Schedule Plan");
            System.out.println("10: Delete Flight Schedule Plan");
            System.out.println("11: Back\n");
            response = 0;
            
            while(response < 1 || response > 9){
                System.out.println("> ");
                response = scanner.nextInt();
                if(response == 1){
                    doCreateFlight();
                }else if(response == 2){
                    doViewAllFlights();
                }else if(response == 3){
                    doViewFlightDetails();
                }else if(response == 4){
                    doUpdateFlight();
                }else if(response == 5){
                    doDeleteFlight();
                }else if(response == 6){
                    System.out.println("11: Back\n");
                    //doCreateFlightSchedulePlan();
                }else if(response == 7){
                    System.out.println("11: Back\n");
                    //doViewAllFlightSchedulePlans();
                }else if(response == 8){
                    System.out.println("11: Back\n");
                    //doViewFlightSchedulePlansDetails();
                }else if(response == 9){
                    System.out.println("11: Back\n");
                    //updateFlightSchedulePlan();
                }else if(response == 10){
                    System.out.println("11: Back\n");
                    //deleteFlightSchedulePlan();
                } else if(response == 10) {
                    break;
                }else{
                    System.out.println("Invalid option, please ty again!\n");
                }
            }
            if(response == 10){
                break;
            }
        }
    }
    
    //make change: new route shld not be disabled √
    //prompt user if need to make complementary flight
    public void doCreateFlight() {
        Scanner sc = new Scanner(System.in);
        Flight flight = new Flight();
        flight.setIsDisabled(false);
            
        System.out.println("*** Create a new Flight ***");
        System.out.println("Enter a flight number > ");
        String flightNum = sc.nextLine().trim();
        flight.setFlightNum(flightNum);
        System.out.println("*** View All Flight Routes ***\n");
        List<Route> allRoutes = flightRouteSessionBeanRemote.retrieveAllFlightRoutes();
        for (Route r: allRoutes) {
            if (r.isHasOppRoute() == false) {
                System.out.println(r.getRouteId() +  " " + r.getOrigin().getAirportName() + " & " + r.getDestination().getAirportName() + "\n");
            } else {
                System.out.println(r.getRouteId() + " " + r.getOrigin().getAirportName() + " & " + r.getDestination().getAirportName());
                System.out.println(r.getDestination().getAirportName() + " & " + r.getOrigin().getAirportName() + "\n");
            }
        }
        
        System.out.println("Select the routeId of the route you would like for your flight");
        Long routeId = sc.nextLong();
        sc.nextLine();
        try {
            Route route = flightRouteSessionBeanRemote.retrieveFlightRoutebyId(routeId);
            flight.setRoute(route);
        } catch (FlightRouteNotFoundException ex) {
            System.out.println("You have inputted the wrong Id: " + ex.getMessage());
        }
        
        System.out.println("**View All Aircraft Configuration**\n");
        List<AircraftConfiguration> acConfigurations = aircraftConfigurationSessionBeanRemote.retrieveAllAircraftConfiguration();
        
        for (AircraftConfiguration ac : acConfigurations) {
            System.out.println(ac.getAircraftConfigurationId() + "\n");
        }
        System.out.print("Type the aircraft configuration name you would prefer for this flight > ");
        Long acId = sc.nextLong();
        sc.nextLine();
        try {
            AircraftConfiguration ac = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationbyId(acId);
            ac.getFlights().add(flight);
            flight.setAircraftConfiguration(ac);
            flightSessionBeanRemote.createNewFlight(flight);
            flight.getRoute().getFlights().add(flight);
            if (flight.getRoute().isHasOppRoute()) {
                flight.setHasReturn(true);
            }
            
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("This configuration does not exist: " + ex.getMessage());
        } catch (FlightExistException ex) {
            System.out.println("This flight already exists: " + ex.getMessage());
        } catch (GeneralException ex) {
            System.out.println("There is a general exception: " + ex.getMessage());
        } catch (RouteDisabledException ex) {
            System.out.println(ex.getMessage());
        }
        

       
        
        
        
    }
    
    public void doViewAllFlights() {
        
        List<Flight> allFlights = flightSessionBeanRemote.retrieveAllFlight();
        for (Flight f : allFlights) {
            System.out.println(f.getFlightNum());
        }

    }
    public void doViewFlightDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** View all flights ***");
        doViewAllFlights();
        System.out.println("Pick the flight number you would like to view details for");
        String flightNum = sc.nextLine().trim();
        
        try {
            Flight flight = flightSessionBeanRemote.retrieveFlightByName(flightNum);
            System.out.printf("%5s %3s %s %3s %30s", flightNum, flight.getRoute().getOrigin().getAirportName(), '&',flight.getRoute().getDestination().getAirportName(), flight.getAircraftConfiguration().getAircraftConfigurationName());
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (FlightUpdateException ex) {
            System.out.println(ex.getMessage());
        }
            
    }
    
    //make change: new route shld not be disabled
    public void doUpdateFlight() {
        Scanner sc = new Scanner(System.in);
        doViewAllFlights();
        System.out.println("Which flight number would you like to update?");
        String flightNum = sc.nextLine().trim();
        System.out.println("Which route would you like to change to?");
        
        Flight flight = new Flight();
        flight.setFlightNum(flightNum);
        Long routeId = sc.nextLong();
        try {
            //to be changed
            Route route = flightRouteSessionBeanRemote.retrieveFlightRoutebyId(routeId);
            flight.setRoute(route);
        } catch (FlightRouteNotFoundException ex) {
            System.out.println("You have inputted the wrong Id: " + ex.getMessage());
        }
        
        System.out.print("Type the aircraft configuration name you would prefer for this flight > ");
        String acName = sc.nextLine().trim();
        try {
            AircraftConfiguration ac = aircraftConfigurationSessionBeanRemote.retrieveAircraftConfigurationbyName(acName);
            flight.setAircraftConfiguration(ac);
        } catch (AircraftConfigurationNotFoundException ex) {
            System.out.println("This configuration does not exist: " + ex.getMessage());
        }
        try {
            flightSessionBeanRemote.updateFlight(flight);
            System.out.println("Flight successfully updated!");
        } catch (FlightUpdateException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println("Inpute invalid");
        } catch (FlightNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public void doDeleteFlight() {
        
        Scanner sc = new Scanner(System.in);
        List<Flight> allFlights = flightSessionBeanRemote.retrieveAllFlight();
        for (Flight f : allFlights) {
            System.out.println(f.getFlightId() + " : " + f.getFlightNum());
        }
        
        System.out.println("Which flight ID would you like to delete?");
        System.out.println("do enter the flight ID and not the flight number!!!");
        Long flightId = sc.nextLong();
        
        try {
            flightSessionBeanRemote.deleteFlight(flightId);
             System.out.println("Done.");
        } catch (FlightNotFoundException ex) {
             System.out.println(ex.getMessage());
        }
    }
    
    
    //need to make changes to ensure no overlap when creating plan
   public void doCreateFlightSchedulePlan() {
        Scanner sc = new Scanner(System.in);
        doViewAllFlights();
        System.out.println("Which flight number do you want to create a plan for?");
        String flightNum = sc.nextLine().trim();
        FlightSchedulePlan fp = new FlightSchedulePlan();
        fp.setFlightNum(flightNum);
        try {
            Flight flight = flightSessionBeanRemote.retrieveFlightByName(flightNum);
            fp.setFlight(flight);
            flight.getFlightSchedulePlans().add(fp);
        } catch ( FlightNotFoundException ex ) {
            System.out.println(ex.getMessage());
        } catch (FlightUpdateException ex) {
            System.out.println(ex.getMessage());
        }
        
        
        
        System.out.println("Which type of schedule would you like to create?");
        System.out.println("1: Single Schedule");
        System.out.println("2: Multiple Schedule");
        System.out.println("3: Recurrent Weekly Schedule");
        System.out.println("4: Recurrent every N days Schedule");
        int scheduleType = sc.nextInt();
        List<Schedule> schedList = new ArrayList<>();
        if (scheduleType == 1) {
            fp.setScheduleType("single_schedule");
            Schedule schedule = new Schedule();
            schedule.setFlightSchedulePlan(fp);
            schedule.setTotalBalanceInventory(fp.getFlight().getAircraftConfiguration().getMaximumCapacity());
            schedule.setTotalAvailableSeat(fp.getFlight().getAircraftConfiguration().getMaximumCapacity());
            schedule.setCabins(fp.getFlight().getAircraftConfiguration().getCabinClass());
            System.out.println("What is your departure day? Answer in DD-MM-YYYY form");
            //schedule.setDepartureDate();
            System.out.println("What is your departure time? Answer in HH:MM");
            //set in schedule
            //convert this to military timing
            //set as first time in flight schedule plan
            System.out.println("What is your flight duration?");
            //set in schedule
            
            if (fp.getFlight().isHasReturn()) {
                System.out.println("***Layover Duration***");
                System.out.println("How many hours would you want your layover duration to be?");
                int hours = sc.nextInt();
                System.out.println("How many minutes would you want your layover duration to be?");
                int minutes = sc.nextInt();
                sc.nextLine();
                int totalMinutes = hours*60+minutes;
                //generate a complementary flight schedule plan
                //fp.setHaveReturn to true  
            }
            System.out.println("***Fares***");
            List<Fare> fareList = new ArrayList<>();
            int x = fp.getFlight().getAircraftConfiguration().getNumCabinClass();
            System.out.println("Enter your fares. Note that you should enter at least " + x + "fares for each cabin class in this flight. Click the letter Q when you are done");
            while (true) {
                String idk = sc.nextLine().trim();
                if (idk.equalsIgnoreCase("Q")) {
                    break;
                }
                System.out.println("Cabin class , FareBasisCode, FareAmount >");
                Fare fare = new Fare(idk, sc.nextLine().trim(), sc.nextInt());
                sc.nextLine();
                fareList.add(fare);
            }
            
            fp.setFares(fareList);
            //schedList.add(schedule)
            fp.setSchedules(schedList);
            
            try {
                flightSchedulePlanSessionBeanRemote.createNewFlightSchedulePlan(fp);
            } catch (FlightPlanExistsException ex) {
                System.out.println(ex.getMessage());
            } catch (UnknownPersistenceException ex) {
                System.out.println(ex.getMessage());
            } catch (FlightDisabledException ex) {
                System.out.println(ex.getMessage());
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage());
            }
        } else if (scheduleType == 2) {
            
        } else if (scheduleType == 3) {
            
        } else {
            System.out.println("Enter how many days (n) you want the schedule to recur");
        }
    }
    
    public void doViewAllFlightSchedulePlans() {
        List<FlightSchedulePlan> planList = flightSchedulePlanSessionBeanRemote.retrieveAllFlightSchedulePlans();
        for (FlightSchedulePlan plan : planList) {
            System.out.println(plan.getFlightSchedulePlanId() + " " + plan.getFlightNum() + " " + plan.getScheduleType());
        }   
    }
    
    //update details
    public void doViewFlightSchedulePlansDetails() {
        Scanner sc = new Scanner(System.in);
        doViewAllFlightSchedulePlans();
        System.out.println("Enter the ID of the plan you would like to view.");
        Long planId = sc.nextLong();
        
        try {
           FlightSchedulePlan fp = flightSchedulePlanSessionBeanRemote.retrieveFlighSchedulePlanById(planId);
           System.out.println("Enter all details here");
        } catch (FlightSchedulePlanNotFoundException ex) {
           System.out.println(ex.getMessage()); 
        }
        
    }
    
    //need to make changes to ensure no overlap when creating plan
    //prompt for new flight number,scheduletype, schedule, fares for each cabin class, 
    public void updateFlightSchedulePlan() {
        Scanner sc = new Scanner(System.in);
        doViewAllFlightSchedulePlans();
        FlightSchedulePlan fr = new FlightSchedulePlan();
        System.out.println("Which flight schedule plan ID would you like to update?");
        Long flightPlanId = sc.nextLong();
        System.out.println("Which flight schedule plan ID would you like to update?");
        
        try {
            flightSchedulePlanSessionBeanRemote.updateFlightSchedulePlan(fr, flightPlanId);
            //FlightSchedulePlanNotFoundException, FlightPlanUpdateException, InputDataValidationException, FlightDisabledException
        } catch (FlightDisabledException ex) {
            System.out.println(ex.getMessage());
        } catch (FlightPlanUpdateException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (FlightSchedulePlanNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    public void deleteFlightSchedulePlan() {
        Scanner sc = new Scanner(System.in);
        List<FlightSchedulePlan> allPlans = flightSchedulePlanSessionBeanRemote.retrieveAllFlightSchedulePlans();
        for (FlightSchedulePlan f : allPlans) {
            System.out.println(f.getFlightSchedulePlanId() + " : " + f.getFlightNum());
        }
        
        System.out.println("Which FlightSchedulePlan ID would you like to delete?");
        System.out.println("do enter the flight schedule plan ID and not the flight number!!!");
        Long flightId = sc.nextLong();
        try {
            FlightSchedulePlan flight = flightSchedulePlanSessionBeanRemote.retrieveFlighSchedulePlanById(flightId);
           
            flightSchedulePlanSessionBeanRemote.deleteFlightSchedulePlan(flightId);
                
            System.out.println("Flight successfully deleted.");
                  
        } catch (FlightSchedulePlanNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
    
            
        
    }
    
    
}
