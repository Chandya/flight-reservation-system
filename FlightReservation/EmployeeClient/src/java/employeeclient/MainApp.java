/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeeclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Airport;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import entity.Customer;
import entity.Employee;
import entity.Flight;
import entity.Partner;
import entity.Schedule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.exception.InvalidUserRoleException;

/**
 *
 * @author Jarryl Yeo
 */
public class MainApp {
    
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private AircraftSessionBeanRemote aircraftSessionBeanRemote;
    private Employee currentEmployee;
    private Partner partnerLogin;
    private FlightOperationModule flightOperationModule;
    private FlightPlanningModule flightPlanningModule;
    private SalesManagementModule salesManagementModule;

    public MainApp() {
    }

    public MainApp(ReservationSessionBeanRemote reservationSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, AirportSessionBeanRemote airportSessionBeanRemote, AircraftSessionBeanRemote aircraftSessionBeanRemote) {
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.aircraftSessionBeanRemote = aircraftSessionBeanRemote;
        
    
    }
    
    public void runApp() throws ParseException{
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            System.out.println("*** Welcome to Flight Employee System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit");
            response = 0;
            
            while(response < 1 || response > 2) {
                System.out.println(">");
                response = scanner.nextInt();
            }
            if(response == 1){
                try{
                    doLogin();
                    System.out.println("Login Successful!\n");
                    salesManagementModule = new SalesManagementModule(flightRouteSessionBeanRemote, flightSchedulePlanSessionBeanRemote, flightScheduleSessionBeanRemote, flightSessionBeanRemote, employeeSessionBeanRemote, aircraftConfigurationSessionBeanRemote,aircraftSessionBeanRemote, currentEmployee);
                    flightOperationModule = new FlightOperationModule(flightRouteSessionBeanRemote, flightSchedulePlanSessionBeanRemote, flightScheduleSessionBeanRemote, flightSessionBeanRemote, employeeSessionBeanRemote, aircraftConfigurationSessionBeanRemote, aircraftSessionBeanRemote, currentEmployee);
                    flightPlanningModule = new FlightPlanningModule(flightRouteSessionBeanRemote, flightSchedulePlanSessionBeanRemote, flightScheduleSessionBeanRemote, flightSessionBeanRemote, employeeSessionBeanRemote, aircraftConfigurationSessionBeanRemote, aircraftSessionBeanRemote, currentEmployee, airportSessionBeanRemote);
                    menuMain();
                }
                catch(InvalidLoginCredentialException ex){
                    System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                }
            }else if(response == 2){
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException{
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("*** Flight System :: Login ***\n");
        System.out.println("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.println("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0){
            currentEmployee = employeeSessionBeanRemote.employeeLogin(username, password);
        }else{
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void menuMain(){
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            System.out.println("*** Flight System ***\n");
            System.out.println("You are login as " + currentEmployee.getEmployeeName() + " with " + currentEmployee.getUserRole().toString() + " rights\n");
            System.out.println("1: Flight Planning Module");
            System.out.println("2: Flight Operation Module");
            System.out.println("3: Sales Management Module");
            System.out.println("4: Logout");
            response = 0;
            
            while(response < 1 || response > 4){
                System.out.println("> ");
                response = scanner.nextInt();
                
                if(response == 1){
                    try{
                        flightPlanningModule.menuFlightPlanningModule();
                    }catch(InvalidUserRoleException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 2){
                    try{
                        flightOperationModule.menuFlightOperationModule();
                    }catch(InvalidUserRoleException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 3) {
                    try{
                        salesManagementModule.menuSalesManagementModule();
                    }catch(InvalidUserRoleException ex) {
                        System.out.println("Invalid option, please try again: " + ex.getMessage() + "\n");
                    }
                }
                else if(response == 4){
                    break;
                }else{
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if(response == 4){
                break;
            }
        }
    }
}
