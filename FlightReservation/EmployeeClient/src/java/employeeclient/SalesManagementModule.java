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
import entity.Employee;
import java.util.Scanner;
import util.enumeration.UserRole;
import util.exception.InvalidUserRoleException;

/**
 *
 * @author Jarryl Yeo
 */
public class SalesManagementModule {
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private AircraftSessionBeanRemote aircraftSessionBeanRemote;
    private Employee currentEmployee;
    
    public SalesManagementModule(){
    }
    
    public SalesManagementModule(FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote, AircraftSessionBeanRemote aircraftSessionBeanRemote, Employee currentEmployee){
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
    public void menuSalesManagementModule() throws InvalidUserRoleException{
        
        if(currentEmployee.getUserRole() != UserRole.SALESMANAGER){
            throw new InvalidUserRoleException("You don't have Sales Manager Rights to access the flight planning module");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            System.out.println("*** Employee Flight System :: Sales Management ***\n");
            System.out.println("1: View Seat Inventory");
            System.out.println("2: View Flight Reservation");
            System.out.println("3: Back\n");
            response = 0;
            while(response < 1 || response > 3){
                System.out.println("> ");
                response = scanner.nextInt();
                if(response == 1){
                    System.out.println("dosomething1");
                }else if(response == 2){
                    System.out.println("dosomething2");
                }else if(response == 3) {
                    break;
                }else{
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if(response == 3){
                break;
            }
        }
    }
}
