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
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Jarryl Yeo
 */
@Entity
public class FlightSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightSchedulePlanId;
    @ManyToOne
    private Flight flight;
    @OneToMany(mappedBy = "flightSchedulePlan", cascade = CascadeType.ALL)
    private List<Schedule> schedules;
    private String scheduleType;
    private boolean haveReturn;
    private boolean isDisabled;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date firstTime;
    private String flightNum;
    private boolean isUsed;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Fare> fares;

    public FlightSchedulePlan() {
        this.schedules = new ArrayList<>();
        this.isDisabled = false;
        this.isUsed = false;
    }

    public void addSchedule(Schedule temp) {
        schedules.add(temp);
    }

    public Long getFlightSchedulePlanId() {
        return flightSchedulePlanId;
    }

    public void setFlightSchedulePlanId(Long flightSchedulePlanId) {
        this.flightSchedulePlanId = flightSchedulePlanId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightSchedulePlanId != null ? flightSchedulePlanId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightSchedulePlanId
        // fields are not set
        if (!(object instanceof FlightSchedulePlan)) {
            return false;
        }
        FlightSchedulePlan other = (FlightSchedulePlan) object;
        if ((this.flightSchedulePlanId == null && other.flightSchedulePlanId != null)
                || (this.flightSchedulePlanId != null
                        && !this.flightSchedulePlanId.equals(other.flightSchedulePlanId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.FlightSchedulePlan[ id=" + flightSchedulePlanId + " ]";
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
        this.flightNum = flight.getFlightNum();
        this.haveReturn = flight.isHasReturn();
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public boolean isHaveReturn() {
        return haveReturn;
    }

    public void setHaveReturn(boolean haveReturn) {
        this.haveReturn = haveReturn;
    }

    /**
     * @return the firstTime
     */
    public Date getFirstTime() {
        return firstTime;
    }

    /**
     * @param firstTime the firstTime to set
     */
    public void setFirstTime(Date firstTime) {
        this.firstTime = firstTime;
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
     * @return the fares
     */
    public List<Fare> getFares() {
        return fares;
    }

    /**
     * @param fares the fares to set
     */
    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    /**
     * @return the isUsed
     */
    public boolean isIsUsed() {
        return isUsed;
    }

    /**
     * @param isUsed the isUsed to set
     */
    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    

}
