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
import java.text.ParseException;
import javax.ejb.EJB;
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
public class Main {

    @EJB(name = "AirportSessionBeanRemote")
    private static AirportSessionBeanRemote airportSessionBeanRemote;

    @EJB(name = "AircraftSessionBeanRemote")
    private static AircraftSessionBeanRemote aircraftSessionBeanRemote;

    @EJB(name = "FlightSessionBeanRemote")
    private static FlightSessionBeanRemote flightSessionBeanRemote;

    @EJB(name = "ReservationSessionBeanRemote")
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;

    @EJB(name = "PartnerSessionBeanRemote")
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;

    @EJB(name = "FlightScheduleSessionBeanRemote")
    private static FlightScheduleSessionBeanRemote flightScheduleSessionBeanRemote;

    @EJB(name = "FlightSchedulePlanSessionBeanRemote")
    private static FlightSchedulePlanSessionBeanRemote flightSchedulePlanSessionBeanRemote;

    @EJB(name = "FlightRouteSessionBeanRemote")
    private static FlightRouteSessionBeanRemote flightRouteSessionBeanRemote;

    @EJB(name = "CustomerSessionBeanRemote")
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    @EJB(name = "AircraftConfigurationSessionBeanRemote")
    private static AircraftConfigurationSessionBeanRemote aircraftConfigurationSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException, CustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException, ReservationNotFoundException, ReservationExistException, GeneralException {
        MainApp mainApp = new MainApp(airportSessionBeanRemote,aircraftSessionBeanRemote, reservationSessionBeanRemote, partnerSessionBeanRemote, flightSessionBeanRemote, flightScheduleSessionBeanRemote, flightSchedulePlanSessionBeanRemote, flightRouteSessionBeanRemote, customerSessionBeanRemote, aircraftConfigurationSessionBeanRemote);
        mainApp.runApp();
        // TODO code application logic here
    }
    
}
