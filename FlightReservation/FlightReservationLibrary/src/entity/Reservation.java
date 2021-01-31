/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author chandya
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    private Schedule reservedSchedule;
    private Customer reservedCustomer;

    public Reservation() {
    }

    public Reservation(Schedule reservedSchedule, Customer reservedCustomer) {
        this.reservedSchedule = reservedSchedule;
        this.reservedCustomer = reservedCustomer;
    }
    
    
    //private String seatNumber;
    //@ManyToOne
    //private Fare fare;

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    /**
     * @return the seatNumber
     */
    //public String getSeatNumber() {
        //return seatNumber;
    //}

    /**
     * @param seatNumber the seatNumber to set
     */
    //public void setSeatNumber(String seatNumber) {
        //this.seatNumber = seatNumber;
    //}


    /**
     * @return the fare
     */
    //public Fare getFare() {
        //return fare;
    //}

    /**
     * @param fare the fare to set
     */
    //public void setFare(Fare fare) {
        //this.fare = fare;
    //}

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }

    public Schedule getReservedSchedule() {
        return reservedSchedule;
    }

    public void setReservedSchedule(Schedule reservedSchedule) {
        this.reservedSchedule = reservedSchedule;
    }

    public Customer getReservedCustomer() {
        return reservedCustomer;
    }

    public void setReservedCustomer(Customer reservedCustomer) {
        this.reservedCustomer = reservedCustomer;
    }
    
}
