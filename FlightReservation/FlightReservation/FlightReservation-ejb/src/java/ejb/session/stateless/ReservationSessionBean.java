/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.GeneralException;
import util.exception.ReservationExistException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Jarryl Yeo
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "FlightReservation-ejbPU")
    private EntityManager em;

    @Override
    public Reservation createNewReservation(Reservation newReservation) throws ReservationExistException, GeneralException{
        try{
            em.persist(newReservation);
            em.flush();
            
            return newReservation;
        }
        catch(PersistenceException ex){
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
            {
                throw new ReservationExistException("Reservation with same identification number already exist");
            }
            else
            {
                throw new GeneralException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
    }
    
    @Override
    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException{
        Reservation reservationToRetrieve = em.find(Reservation.class, reservationId);
        if(reservationToRetrieve != null){
            return reservationToRetrieve;
        }else{
            throw new ReservationNotFoundException("Reservation ID " + reservationId + " does not exist!");
        }
    }
    
    @Override
    public List<Reservation> retrieveAllReservation(){
        Query query = em.createQuery("SELECT r FROM reservation r");
        return query.getResultList();
    }
    
    @Override
    public List<Reservation> retrieveReservationByCustomer(Customer customerReservation){
        Query query = em.createQuery("SELECT r FROM Reservation WHERE r.reservedCustomer = :inCustomer");
        query.setParameter("inCustomer", customerReservation);
        return query.getResultList();
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
