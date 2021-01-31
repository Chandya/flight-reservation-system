/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author chandya
 */
@Entity
public class Schedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long scheduleId;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date departureDate;
    // in the format hour:minute military timing
    private String departureTime;
    // in minutes
    private int estimatedDuration;

    private int totalBalanceInventory = 0;
    @OneToMany
    private List<CabinClass> cabins;

    @ManyToOne
    private FlightSchedulePlan flightSchedulePlan;
    private int totalAvailableSeat = 0;
    private int totalReservedSeat = 0;

    public Schedule() {
        this.cabins = new ArrayList<>();
    }

    public Schedule(Date departureDate, int estimatedDuration) {
        this.departureDate = departureDate;
        this.estimatedDuration = estimatedDuration;
        this.totalBalanceInventory = 0;
        this.totalAvailableSeat = 0;
        this.totalReservedSeat = 0;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public FlightSchedulePlan getFlightSchedulePlan() {
        return flightSchedulePlan;
    }

    public void setFlightSchedulePlan(FlightSchedulePlan flightSchedulePlan) {
        this.flightSchedulePlan = flightSchedulePlan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scheduleId != null ? scheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the scheduleId fields are
        // not set
        if (!(object instanceof Schedule)) {
            return false;
        }
        Schedule other = (Schedule) object;
        if ((this.scheduleId == null && other.scheduleId != null)
                || (this.scheduleId != null && !this.scheduleId.equals(other.scheduleId))) {
            return false;
        }
        return true;
    }

    public void addFare(CabinClass temp) {
        cabins.add(temp);
    }

    @Override
    public String toString() {
        return "entity.Schedule[ id=" + scheduleId + " ]";
    }

    /**
     * @return the departureDate
     */
    public Date getDepartureDate() {
        return departureDate;
    }

    /**
     * @param departureDate the departureDate to set
     */
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    /**
     * @return the estimatedDuration
     */
    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    /**
     * @param estimatedDuration the estimatedDuration to set
     */
    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    /**
     * @return the cabins
     */
    public List<CabinClass> getCabins() {
        return cabins;
    }

    /**
     * @param cabins the cabins to set
     */
    public void setCabins(List<CabinClass> cabins) {
        this.cabins = cabins;
    }

    public int getTotalBalanceInventory() {
        for (CabinClass c : cabins) {
            this.totalBalanceInventory += c.getBalanceSeat();
        }
        return totalBalanceInventory;
    }

    public void setTotalBalanceInventory(int totalBalanceInventory) {
        this.totalBalanceInventory = totalBalanceInventory;
    }

    public int getTotalAvailableSeat() {
        for (CabinClass c : cabins) {
            this.totalAvailableSeat += c.getAvailableSeat();
        }
        return totalAvailableSeat;
    }

    public void setTotalAvailableSeat(int totalAvailableSeat) {
        this.totalAvailableSeat = totalAvailableSeat;
    }

    public int getTotalReservedSeat() {
        for (CabinClass c : cabins) {
            this.totalReservedSeat += c.getReservedSeat();
        }
        return totalReservedSeat;
    }

    public void setTotalReservedSeat(int totalReservedSeat) {
        this.totalReservedSeat = totalReservedSeat;
    }

    /**
     * @return the departureTime
     */
    public String getDepartureTime() {
        return departureTime;
    }

    /**
     * @param departureTime the departureTime to set
     */
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

}
