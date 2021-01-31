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
import javax.persistence.OneToOne;

/**
 *
 * @author Jarryl Yeo
 */
@Entity
public class Aircraft implements Serializable {





    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftId;
    @Column(nullable = false)
    private String aircraftType;
    @Column(nullable = false)
    private int maxNoSeat;
   

    public Aircraft() {
        
    }

    public Aircraft(String aircraftType, int maxNoSeat) {
        this.aircraftType = aircraftType;
        this.maxNoSeat = maxNoSeat;
    } 

    public Long getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(Long aircraftId) {
        this.aircraftId = aircraftId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftId != null ? aircraftId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the aircraftId fields are not set
        if (!(object instanceof Aircraft)) {
            return false;
        }
        Aircraft other = (Aircraft) object;
        if ((this.aircraftId == null && other.aircraftId != null) || (this.aircraftId != null && !this.aircraftId.equals(other.aircraftId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Aircraft[ id=" + aircraftId + " ]";
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public int getMaxNoSeat() {
        return maxNoSeat;
    }

    public void setMaxNoSeat(int maxNoSeat) {
        this.maxNoSeat = maxNoSeat;
    }

   
    
}
