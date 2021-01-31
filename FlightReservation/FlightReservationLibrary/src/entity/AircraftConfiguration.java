/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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
public class AircraftConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aircraftConfigurationId;

    @Column(nullable = false)
    private String aircraftConfigurationName;

    private int maximumCapacity;

    @Column(nullable = false)
    private int numCabinClass;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CabinClass> cabinClass;
    @OneToMany(mappedBy = "aircraftConfiguration")
    private List<Flight> flights;

    public Long getAircraftConfigurationId() {
        return aircraftConfigurationId;
    }

    public AircraftConfiguration() {
        this.cabinClass = new ArrayList<>();
        this.flights = new ArrayList<>();
    }

    public AircraftConfiguration(String name, int numCabinClass, int maximumCapacity) {
        this.aircraftConfigurationName = name;
        this.numCabinClass = numCabinClass;
        this.maximumCapacity = maximumCapacity;
    }

    public void setAircraftConfigurationId(Long aircraftConfigurationId) {
        this.aircraftConfigurationId = aircraftConfigurationId;
    }

    /**
     * @return the aircraftConfigurationName
     */
    public String getAircraftConfigurationName() {
        return aircraftConfigurationName;
    }

    /**
     * @param aircraftConfigurationName the aircraftConfigurationName to set
     */
    public void setAircraftConfigurationName(String aircraftConfigurationName) {
        this.aircraftConfigurationName = aircraftConfigurationName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aircraftConfigurationId != null ? aircraftConfigurationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the
        // aircraftConfigurationId fields are not set
        if (!(object instanceof AircraftConfiguration)) {
            return false;
        }
        AircraftConfiguration other = (AircraftConfiguration) object;
        if ((this.aircraftConfigurationId == null && other.aircraftConfigurationId != null)
                || (this.aircraftConfigurationId != null
                        && !this.aircraftConfigurationId.equals(other.aircraftConfigurationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AircraftConfiguration[ id=" + aircraftConfigurationId + " ]";
    }

    public void addCabinClass(CabinClass temp) {
        this.cabinClass.add(temp);
    }

    public int getNumCabinClass() {
        return numCabinClass;
    }

    public void setNumCabinClass(int numCabinClass) {
        this.numCabinClass = numCabinClass;
    }

    public List<CabinClass> getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(List<CabinClass> cabinClass) {
        this.cabinClass = cabinClass;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    /**
     * @return the flights
     */
    public List<Flight> getFlights() {
        return flights;
    }

    /**
     * @param flights the flights to set
     */
    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
