/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import java.util.List;
import javax.ejb.Local;
import util.exception.GeneralException;
import util.exception.ReservationExistException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Jarryl Yeo
 */
@Local
public interface ReservationSessionBeanLocal {
    public Reservation createNewReservation(Reservation newReservation) throws ReservationExistException, GeneralException;

    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;

    public List<Reservation> retrieveAllReservation();

    public List<Reservation> retrieveReservationByCustomer(Customer customerReservation);
    
}
