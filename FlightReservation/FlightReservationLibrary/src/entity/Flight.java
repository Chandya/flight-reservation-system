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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author chandya
 */
@Entity
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;
    private String flightNum;

    // not sure if the relationship should be bidirectional

    @ManyToOne
    private AircraftConfiguration aircraftConfiguration;

    @ManyToOne
    private Route route;

    private boolean isDisabled;

    private boolean hasReturn;
    @OneToMany(mappedBy = "flight")
    private List<FlightSchedulePlan> flightSchedulePlans;

    public Flight() {
        this.flightSchedulePlans = new ArrayList<>();

    }

    public Flight(String flightNum, Route route, AircraftConfiguration aircraftConfiguration) {
        this.flightNum = flightNum;
        this.aircraftConfiguration = aircraftConfiguration;
        this.isDisabled = false;
        this.hasReturn = false;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightId != null ? flightId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightId fields are
        // not set
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.flightId == null && other.flightId != null)
                || (this.flightId != null && !this.flightId.equals(other.flightId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Flight[ id=" + flightId + " ]";
    }

    /**
     * @return the flightNum
     */
    public String getFlightNum() {
        return flightNum;
    }

    /**
     * @param flightNum the flightNum to set
     */
    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }

    /**
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * @return the isDisabled
     */
    public boolean isIsDisabled() {
        return isDisabled;
    }

    /**
     * @param isDisabled the isDisabled to set
     */
    public void setIsDisabled(boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    /**
     * @return the aircraftConfiguration
     */
    public AircraftConfiguration getAircraftConfiguration() {
        return aircraftConfiguration;
    }

    /**
     * @param aircraftConfiguration the aircraftConfiguration to set
     */
    public void setAircraftConfiguration(AircraftConfiguration aircraftConfiguration) {
        this.aircraftConfiguration = aircraftConfiguration;
    }

    /**
     * @return the hasReturn
     */
    public boolean isHasReturn() {
        return hasReturn;
    }

    /**
     * @param hasReturn the hasReturn to set
     */
    public void setHasReturn(boolean hasReturn) {
        this.hasReturn = hasReturn;
    }

    /**
     * @return the flightSchedulePlans
     */
    public List<FlightSchedulePlan> getFlightSchedulePlans() {
        return flightSchedulePlans;
    }

    /**
     * @param flightSchedulePlans the flightSchedulePlans to set
     */
    public void setFlightSchedulePlans(List<FlightSchedulePlan> flightSchedulePlans) {
        this.flightSchedulePlans = flightSchedulePlans;
    }

}
