/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Jarryl Yeo
 */
@Entity
public class CabinClass implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long cabinClassId;

    private int numRow;
    private int numAisle;
    private int numSeatsAbreast;
    private String actualSeatingConfiguration;
    private int maxSeatCapacity;
    private int availableSeat;
    private int reservedSeat;
    private int balanceSeat;
    private String type;
    
    //@ManyToOne
    //private AircraftConfiguration aircraftConfiguration;

    public CabinClass() {
    }

    public CabinClass(String type, int numAisle, int numRow, int numSeatsAbreast, String actualSeatingConfiguration, int maxSeatCapacity) {
        this.type = type;
        this.numRow = numRow;
        this.numAisle = numAisle;
        this.numSeatsAbreast = numSeatsAbreast;
        this.actualSeatingConfiguration = actualSeatingConfiguration;
        this.maxSeatCapacity = maxSeatCapacity;
        this.availableSeat = maxSeatCapacity;
        this.reservedSeat = 0;
        this.balanceSeat = maxSeatCapacity;
        //this.aircraftConfiguration = aircraftConfiguration;
    }


    public Long getCabinClassId() {
        return cabinClassId;
    }

    public void setCabinClassId(Long cabinClassId) {
        this.cabinClassId = cabinClassId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cabinClassId != null ? cabinClassId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the cabinClassId fields are not set
        if (!(object instanceof CabinClass)) {
            return false;
        }
        CabinClass other = (CabinClass) object;
        if ((this.cabinClassId == null && other.cabinClassId != null) || (this.cabinClassId != null && !this.cabinClassId.equals(other.cabinClassId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CabinClass[ id=" + cabinClassId + " ]";
    }
    
   

    public int getNumRow() {
        return numRow;
    }

    public void setNumRow(int numRow) {
        this.numRow = numRow;
    }

    public int getNumAisle() {
        return numAisle;
    }

    public void setNumAisle(int numAisle) {
        this.numAisle = numAisle;
    }

    public int getNumSeatsAbreast() {
        return numSeatsAbreast;
    }

    public void setNumSeatsAbreast(int numSeatsAbreast) {
        this.numSeatsAbreast = numSeatsAbreast;
    }

    public String getActualSeatingConfiguration() {
        return actualSeatingConfiguration;
    }

    public void setActualSeatingConfiguration(String actualSeatingConfiguration) {
        this.actualSeatingConfiguration = actualSeatingConfiguration;
    }

    public int getMaxSeatCapacity() {
        return maxSeatCapacity;
    }

    public void setMaxSeatCapacity(int maxSeatCapacity) {
        this.maxSeatCapacity = maxSeatCapacity;
    }

    public int getAvailableSeat() {
        return availableSeat;
    }

    public void setAvailableSeat(int availableSeat) {
        this.availableSeat = availableSeat;
    }

    public int getReservedSeat() {
        return reservedSeat;
    }

    public void setReservedSeat(int reservedSeat) {
        this.reservedSeat = reservedSeat;
    }

    public int getBalanceSeat() {
        return balanceSeat;
    }

    public void setBalanceSeat(int balanceSeat) {
        this.balanceSeat = balanceSeat;
    }
     
    public String getType() {
        return type;
    }

    
    public void setType(String type) {
        this.type = type;
    }
    
}
