/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flightreservationclient;

import ejb.session.stateless.AircraftConfigurationSessionBeanRemote;
import ejb.session.stateless.AircraftSessionBeanRemote;
import ejb.session.stateless.AirportSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.FlightRouteSessionBeanRemote;
import ejb.session.stateless.FlightSchedulePlanSessionBeanRemote;
import ejb.session.stateless.FlightScheduleSessionBeanRemote;
import ejb.session.stateless.FlightSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Airport;
import entity.CabinClass;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;
import entity.Customer;
import entity.Fare;
import entity.Flight;
import entity.Partner;
import entity.Reservation;
import entity.Schedule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerUsernameExistException;
import util.exception.GeneralException;
import util.exception.InputDataValidationException;
import util.exception.ReservationExistException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */

//This is the FRS Reservation Client
public class MainApp {
    
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private PartnerSessionBeanRemote partnerSessionBeanRemote;
    private FlightSessionBeanRemote flightSessionBeanRemote;
    private FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;
    private FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;
    private FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;
    private AirportSessionBeanRemote airportSessionBeanRemote;
    private AircraftSessionBeanRemote aircraftSessionBeanRemote;
    private Customer currentCustomer;
    private Partner partnerLogin;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public MainApp(AirportSessionBeanRemote airportSessionBeanRemote,AircraftSessionBeanRemote aircraftSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, PartnerSessionBeanRemote partnerSessionBeanRemote, FlightSessionBeanRemote flightSessionBeanRemote, FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote, FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote, FlightRouteSessionBeanRemote flightRouteSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote) {
        this();
        this.airportSessionBeanRemote = airportSessionBeanRemote;
        this.aircraftSessionBeanRemote = aircraftSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.partnerSessionBeanRemote = partnerSessionBeanRemote;
        this.flightSessionBeanRemote = flightSessionBeanRemote;
        this.flightScheduleSessionBeanRemote = flightScheduleSessionBeanRemote;
        this.flightSchedulePlanSessionBeanRemote = flightSchedulePlanSessionBeanRemote;
        this.flightRouteSessionBeanRemote = flightRouteSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.aircraftConfigurationSessionBeanRemote = aircraftConfigurationSessionBeanRemote;
    }
    
    public void runApp() throws ParseException, CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException, ReservationNotFoundException, ReservationExistException, GeneralException{
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true){
            System.out.println("*** Welcome to Flight Reservation System ***\n");
            System.out.println("1: Register As Customer");
            System.out.println("2: Customer Login");
            System.out.println("3: Search Flight");
            System.out.println("4: Exit");
            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.println("> ");
                response = scanner.nextInt();
                if(response == 1){
                    doCreateNewCustomer();
                }else if(response == 2){
                    try{
                        doLogin();
                        System.out.println("Login Successful!\n");
                        menuMain();
                    }
                    catch(InvalidLoginCredentialException ex){
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                }else if(response == 3){
                    doSearchFlight();
                }else if(response == 4){
                    break;
                }else{
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if(response == 4){
                System.out.println("Thank you for using Flight Reservation System! Good Bye");
                break;
            }
        }
    }
    //To register new Customer
    private void doCreateNewCustomer(){
        Scanner scanner = new Scanner(System.in);
        Customer newCustomer = new Customer();
        System.out.println("*** Flight Reservation System :: Customer Registration :: Register As Customer ***\n");
        System.out.print("Enter First Name > ");
        newCustomer.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter Last Name > ");
        newCustomer.setLastName(scanner.nextLine().trim());
        System.out.println("Enter Email > ");
        newCustomer.setEmail(scanner.nextLine().trim());
        System.out.println("Enter mobile phone > ");
        newCustomer.setMobileNum(scanner.nextLine().trim());
        System.out.println("Enter Address > ");
        newCustomer.setAddress(scanner.nextLine().trim());
        System.out.print("Enter Username > ");
        newCustomer.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password > ");
        newCustomer.setPassword(scanner.nextLine().trim());
        
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newCustomer);
        
        if(constraintViolations.isEmpty()){
            try{
                Long newCustomerId = customerSessionBeanRemote.createNewCustomer(newCustomer);
                System.out.println("You have successfully registered for FRS! Here is your customer ID: " + newCustomerId + " Enjoy!\n");
            }catch(CustomerUsernameExistException ex){
                System.out.println("An error has occurred while creating the new customer!: The username already exist\n");
            }catch(UnknownPersistenceException ex){
                System.out.println("An error has occurred while creating the new customer!: " + ex.getMessage() +"\n");
            }catch(InputDataValidationException ex){
                    System.out.println(ex.getMessage() + "\n");
            }   
        }else{
            showInputDataValidationErrorsForCustomer(constraintViolations);
        }
    }
    
    private void doLogin() throws InvalidLoginCredentialException{
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("*** POS System :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0){
            currentCustomer = customerSessionBeanRemote.customerLogin(username, password);
        }else{
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    private void doSearchFlight() throws ParseException, CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException, ReservationExistException, GeneralException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Welcome to Search Flight ***\n");
        System.out.println("Please enter Trip Type\n");
        System.out.println("1: Round - Trip");
        System.out.println("2: One - Way Trip");
        System.out.print("> ");
        int tripType = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Departure Airport> ");
        String origin = scanner.nextLine().trim();
        System.out.print("Eneter Destination Airport> ");
        String destination = scanner.nextLine().trim();
        System.out.print("Enter Departure Date in format of (dd-mm-yyyy): ");
        //the trim() might cause some problem
        String tempDate = scanner.nextLine().trim();
        SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
        Date departureDate = format.parse(tempDate);
        Date returnDate = format.parse(tempDate);
        if(tripType == 1){
            System.out.print("Enter Arrival Date in format of (dd-mm-yyyy): ");
            String tempDate2 = scanner.nextLine().trim();
            returnDate = format.parse(tempDate2);
        }
        System.out.print("Enter number of Passenger: ");
        int numOfPassenger = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("Do you wish to have direct flight or connecting flight?\n");
        System.out.println("1: Direct Flight");
        System.out.println("2: Connecting Flight");
        int flightPreference = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Do you have any preference for cabin class?\n");
        System.out.println("F: First Class");
        System.out.println("J: Business Class");
        System.out.println("W: Premium Economy Class");
        System.out.println("Y: Economy Class");
        String cabinClassPreference = scanner.next();
        scanner.nextLine();
        System.out.println("Here are the Results For Departure Date!");
        List<Schedule> filterList = new ArrayList<>();
        //print the departure date list
        List<Schedule> scheduleToShowDay0 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(departureDate, origin, destination);
        Calendar c = Calendar.getInstance();
        c.setTime(departureDate);
        c.add(Calendar.DATE,1);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
        Date departureDate1 = c.getTime();
        //Departure day plus 1
        List<Schedule> scheduleToShowDay1 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(departureDate1, origin, destination);
        c.add(Calendar.DATE,1);
        Date departureDate2 = c.getTime();
        //departure day plus 2
        List<Schedule> scheduleToShowDay2 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(departureDate2, origin, destination);
        c.add(Calendar.DATE,1);
        Date departureDate3 = c.getTime();
        //departure day plus 3
        List<Schedule> scheduleToShowDay3 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(departureDate3, origin, destination);
        c.add(Calendar.DATE,-4);
        Date departureDate4 = c.getTime();
        //departure day minus 1
        List<Schedule> scheduleToShowDay4 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(departureDate4, origin, destination);
        c.add(Calendar.DATE, -1);
        Date departureDate5 = c.getTime();
        //departure day minus 2
        List<Schedule> scheduleToShowDay5 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(departureDate5, origin, destination);
        c.add(Calendar.DATE, -1);
        Date departureDate6 = c.getTime();
        //departure day minus 3
        List<Schedule> scheduleToShowDay6 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(departureDate6, origin, destination);
        
        if(!scheduleToShowDay0.isEmpty()){
            System.out.println("List of Flight available on Departure Date: " + departureDate.toString());
            for(Schedule s : scheduleToShowDay0){
                System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
        if(!scheduleToShowDay1.isEmpty()){
            System.out.println("List of Flight available on Departure Date: " + departureDate1.toString());
            for(Schedule s : scheduleToShowDay1){
                System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
        if(!scheduleToShowDay2.isEmpty()){
            System.out.println("List of Flight available on Departure Date: " + departureDate2.toString());
            for(Schedule s : scheduleToShowDay2){
                System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
        if(!scheduleToShowDay3.isEmpty()){
            System.out.println("List of Flight available on Departure Date: " + departureDate3.toString());
            for(Schedule s : scheduleToShowDay3){
                System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
        if(!scheduleToShowDay4.isEmpty()){
            System.out.println("List of Flight available on Departure Date: " + departureDate4.toString());
            for(Schedule s : scheduleToShowDay4){
                System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
        if(!scheduleToShowDay5.isEmpty()){
            System.out.println("List of Flight available on Departure Date: " + departureDate5.toString());
            for(Schedule s : scheduleToShowDay5){
                System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
        if (!scheduleToShowDay6.isEmpty()){
            System.out.println("List of Flight available on Departure Date: " + departureDate6.toString());
            for(Schedule s : scheduleToShowDay6){
                System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
        //If there is a round trip
        if(tripType == 1){
            System.out.println("Here are the Results For Return Date!");
            //print the departure date list
            List<Schedule> scheduleToShowReturnDay0 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(returnDate, destination, origin);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(returnDate);
            c1.add(Calendar.DATE,1);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-mm-yyyy");
            Date returnDate1 = c1.getTime();
            //Return date plus 1
            List<Schedule> scheduleToShowReturnDay1 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(returnDate1, destination, origin);
            c1.add(Calendar.DATE,1);
            Date returnDate2 = c1.getTime();
            //return day plus 2
            List<Schedule> scheduleToShowReturnDay2 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(returnDate2, destination, origin);
            c1.add(Calendar.DATE,1);
            Date returnDate3 = c1.getTime();
            //return day plus 3
            List<Schedule> scheduleToShowReturnDay3 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(returnDate3, destination, origin);
            c1.add(Calendar.DATE,-4);
            Date returnDate4 = c1.getTime();
            //return day minus 1
            List<Schedule> scheduleToShowReturnDay4 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(returnDate4, destination, origin);
            c1.add(Calendar.DATE, -1);
            Date returnDate5 = c1.getTime();
            //departure day minus 2
            List<Schedule> scheduleToShowReturnDay5 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(returnDate5, destination, origin);
            c1.add(Calendar.DATE, -1);
            Date returnDate6 = c1.getTime();
            //departure day minus 3
            List<Schedule> scheduleToShowReturnDay6 = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(returnDate6, destination, origin);
        
            if(!scheduleToShowReturnDay0.isEmpty()){
                System.out.println("List of Flight available on Return Date: " + returnDate.toString());
                for(Schedule s : scheduleToShowReturnDay0){
                    System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
            if(!scheduleToShowReturnDay1.isEmpty()){
                System.out.println("List of Flight available on Return Date: " + returnDate1.toString());
                for(Schedule s : scheduleToShowReturnDay1){
                    System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
            if(!scheduleToShowReturnDay2.isEmpty()){
                System.out.println("List of Flight available on Return Date: " + returnDate2.toString());
                for(Schedule s : scheduleToShowReturnDay2){
                    System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
            if(!scheduleToShowReturnDay3.isEmpty()){
                System.out.println("List of Flight available on Return Date: " + returnDate3.toString());
                for(Schedule s : scheduleToShowReturnDay3){
                    System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
            if(!scheduleToShowReturnDay4.isEmpty()){
                System.out.println("List of Flight available on Return Date: " + returnDate4.toString());
                for(Schedule s : scheduleToShowReturnDay4){
                    System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
            if(!scheduleToShowReturnDay5.isEmpty()){
                System.out.println("List of Flight available on Return Date: " + returnDate5.toString());
                for(Schedule s : scheduleToShowReturnDay5){
                    System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }
            if(!scheduleToShowReturnDay6.isEmpty()){
                System.out.println("List of Flight available on Return Date: " + returnDate6.toString());
                for(Schedule s : scheduleToShowReturnDay6){
                    System.out.println("Flight Number: " + s.getFlightSchedulePlan().getFlightNum());
                    List<Fare> fares = s.getFlightSchedulePlan().getFares();
                    List<Double> sortedFare = new ArrayList<>();
                    for(Fare f : fares){
                        sortedFare.add(f.getFareAmount());
                    }
                    Collections.sort(sortedFare);
                    System.out.println("Fare: " + sortedFare.get(0));
                    System.out.println("Fare for " + numOfPassenger + " passengers: " + sortedFare.get(0) * numOfPassenger);
                    }
                }    
            }
        //To prevent customer from not logging in
        if(currentCustomer == null){
            System.out.println("Please Login before reserving Flight!");
        }else{
            doReserveFlight();
        }
    }
        
        
                    //List<Flight> flightList = new ArrayList<>();
                    //System.out.println("enter Origin: ");
                    //String origin = scanner.nextLine();
                    //System.out.println("enter Destination: ");
                    //String destination = scanner.nextLine();
                    //Pair pair = new Pair(origin, destination);
                    //System.out.println("enter departure date in this format dd-mm-yyyy: ");
                    //String temp = scanner.nextLine();
                    //SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
                    //Date departureDate = format.parse(temp);    
                    //System.out.println("enter returning date in this format dd-mm-yyyy: ");
                    //String temp2 = scanner.nextLine();
                    //Date returningDate = format.parse(temp2);
                    //System.out.println("enter number of passenger: ");
                    //int numOfPassenger = scanner.nextInt();
                    //flightList = flightSessionBeanRemote.searchFlightReturn(pair, departureDate, returningDate, numOfPassenger);
                    //for(Flight f : flightList){
                        //System.out.println(" " + f.getFlightRoute() + " ");
                    //}
        
    
    private void doReserveFlight() throws ParseException, CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException, ReservationExistException, GeneralException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Welcome to Reservation of Seats ***\n");
        System.out.println("From Search Flight List, choose the Flight Schedule that you like to reserve");
        System.out.print("Enter Departure Airport: ");
        String origin = scanner.nextLine().trim();
        System.out.print("Enter Arrival Airport: ");
        String destination = scanner.nextLine().trim();
        System.out.print("Enter Departure Date in the format of (DD-MM-YYYY): ");
        String tempDate = scanner.nextLine().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
        Date departureDate = sdf.parse(tempDate);
        System.out.print("Enter Arrival Date in the format of (DD-MM-YYYY): ");
        String tempDate1 = scanner.nextLine().trim();
        Date arrivalDate = sdf.parse(tempDate1);
        System.out.println("Do you wish to have a Return Trip?");
        System.out.println("1: Yes");
        System.out.println("2: No");
        int tripType = scanner.nextInt();
        scanner.nextLine();
        List<Schedule> scheduleToTest = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(departureDate, origin, destination);
        Schedule scheduleToReserve = scheduleToTest.get(0);
        if(tripType == 1){
            List<Schedule> returnScheduleToTest = flightScheduleSessionBeanRemote.retrieveAllScheduleBySearchResult(arrivalDate, destination, origin);
            Schedule returnScheduleToReserve = returnScheduleToTest.get(0);
        }
        System.out.println("Enter number of Passenger: ");
        int numOfPassenger = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter your own passport Number: ");
        String currentCustomerPassportNumber = scanner.nextLine();
        currentCustomer.setPassportNumber(currentCustomerPassportNumber);
        for(int i = 0; i < numOfPassenger; i++){
            int temp = i + 1;
            System.out.println("Enter Passenger " + temp + " First Name: ");
            String firstName = scanner.nextLine().trim();
            System.out.println("Enter Passenger " + temp + " Last Name: ");
            String lastName = scanner.nextLine().trim();
            System.out.println("Enter Passenger " + temp + " Passport Number: ");
            String passportNumber = scanner.nextLine().trim();
            Customer customerToAdd = new Customer(firstName, lastName, passportNumber);
            customerSessionBeanRemote.createNewCustomer(customerToAdd);
            currentCustomer.addCustomer(customerToAdd);
        }
        //Include seat number method here
        System.out.println("*** Payment Method ***\n");
        if(currentCustomer.getCreditCardNumber() == " " && currentCustomer.getCVV() == " "){
            System.out.print("Enter Credit Card Number: ");
            String creditCardNumber = scanner.nextLine().trim();
            currentCustomer.setCreditCardNumber(creditCardNumber);
            System.out.print("Enter CVV: ");
            String CVV = scanner.nextLine().trim();
            currentCustomer.setCVV(CVV);
        }else{
            System.out.println("This is your payment Method: " + currentCustomer.getCreditCardNumber() + " and " + currentCustomer.getCVV());
        }
        Reservation reservationForCustomer = reservationSessionBeanRemote.createNewReservation(new Reservation(scheduleToReserve,currentCustomer));
        System.out.println("Reservation has been made!! Here is the Reservation ID: " + reservationForCustomer.getReservationId());
    }
    
    private void doViewFlightReservation(){
        System.out.print("*** List of Flight Reservation ***");
        List<Reservation> reservationsToView = reservationSessionBeanRemote.retrieveReservationByCustomer(currentCustomer);
        System.out.println("Here are the list of Flight Reservation ID");
        for(Reservation r : reservationsToView){
            System.out.println(r.toString());
        }
    }
    
    private void doViewFlightReservationDetails() throws ReservationNotFoundException{
        System.out.print("Enter the Reservation ID: ");
        Scanner scanner = new Scanner(System.in);
        Long reservationIdToRetrieve = scanner.nextLong();
        scanner.nextLine();
        Reservation reservationToViewDetails = reservationSessionBeanRemote.retrieveReservationByReservationId(reservationIdToRetrieve);
        System.out.println("Your Reservation Schedule Flight Number: " + reservationToViewDetails.getReservedSchedule().getFlightSchedulePlan().getFlightNum());
        System.out.println("Your Reservation Schedule Flight Origin: " + reservationToViewDetails.getReservedSchedule().getFlightSchedulePlan().getFlight().getRoute().getOrigin().getAirportName());
        System.out.println("Your Reservation Schedule Flight Destination: " + reservationToViewDetails.getReservedSchedule().getFlightSchedulePlan().getFlight().getRoute().getDestination().getAirportName());
        System.out.println("Your Reservation Schedule Flight Number of Passenger: " + reservationToViewDetails.getReservedCustomer().getPassenger().size());
        List<Fare> fare1 = reservationToViewDetails.getReservedSchedule().getFlightSchedulePlan().getFares();
        List<Double> fareToAdd = new ArrayList<>();
        for(Fare f : fare1){
            fareToAdd.add(f.getFareAmount());
        }
        Collections.sort(fareToAdd);
        Double realFarePaid = fareToAdd.get(0);
        System.out.println("Your Reservation Schedule Flight Fare: " + reservationToViewDetails.getReservedCustomer().getPassenger().size() * realFarePaid);
    }
    private void showInputDataValidationErrorsForCustomer(Set<ConstraintViolation<Customer>>constraintViolations){
        
        System.out.println("\nInput data validation error!:");
        for(ConstraintViolation constraintViolation : constraintViolations){
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }
        System.out.println("\nPlease try again.......\n");   
    }
    
    private void menuMain() throws ParseException, CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException, ReservationNotFoundException, ReservationExistException, GeneralException{
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while(true){
            System.out.println("*** Flight Reservation System ***\n");
            System.out.println("You are login as " +currentCustomer.getFirstName() + " " + currentCustomer.getLastName());
            System.out.println("1: Reserve Flight");
            System.out.println("2: View My Flight Reservation");
            System.out.println("3: View My Flight Reservation Details");
            System.out.println("4: Logout");
            response = 0;
            while(response < 1 || response > 3){
                System.out.println("> ");
                response = scanner.nextInt();
                if(response == 1){
                    doSearchFlight();
                }else if(response == 2){
                    doViewFlightReservation();
                }else if(response == 3){
                    doViewFlightReservationDetails();
                }else if(response == 4){
                    break;
                }else{
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if(response == 4){
                System.out.println("Thank you for using FRS! Have a nice day!");
                break;
            }
        }
    }
}
            
            //while(response < 1 || response > 2){
                //System.out.println(">");
                //response = scanner.nextInt();
                //if(response == 1){
                    //try{
                        //doLogin();
                        //System.out.println("Login successful! \n");
                        
                        
                    //}
                    //catch(InvalidLoginCredentialException ex){
                        //System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    //}
                
            //}
            //}
            
        //}
