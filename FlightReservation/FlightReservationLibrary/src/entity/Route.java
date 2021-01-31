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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


/**
 *
 * @author chandya
 */
@Entity
public class Route implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;
    
    @OneToMany(mappedBy = "route")
    private List<Flight> flights;
    
    @OneToOne
    private Airport origin;
    @OneToOne
    private Airport destination;
    
    private boolean hasOppRoute;
    
    private boolean isDisabled;
    
    private boolean isUsed;
    //to delete if wrong
    private String originName;
    
    //to delete if wrong
    private String destinationName;

    public Route() {
        this.flights = new ArrayList<>();
    }
    
    public Route(Airport origin, Airport destination) {
        this.origin = origin;
        this.destination = destination;
        this.originName = origin.getIATACode();
        this.destinationName = destination.getIATACode();
        this.hasOppRoute = false;
        this.isDisabled = false;
        this.isUsed = false;
    }
 

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
    
  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (routeId != null ? routeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the routeId fields are not set
        if (!(object instanceof Route)) {
            return false;
        }
        Route other = (Route) object;
        if ((this.routeId == null && other.routeId != null) || (this.routeId != null && !this.routeId.equals(other.routeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Route[ id=" + routeId + " ]";
    }

    /**
     * @return the origin
     */
    public Airport getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(Airport origin) {
        this.origin = origin;
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
     * @return the destination
     */
    public Airport getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    /**
     * @return the hasOppRoute
     */
    public boolean isHasOppRoute() {
        return hasOppRoute;
    }

    /**
     * @param hasOppRoute the hasOppRoute to set
     */
    public void setHasOppRoute(boolean hasOppRoute) {
        this.hasOppRoute = hasOppRoute;
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

    /**
     * @return the originName
     */
    public String getOriginName() {
        return originName;
    }

    /**
     * @param originName the originName to set
     */
    public void setOriginName(String originName) {
        this.originName = originName;
    }

    /**
     * @return the destinationName
     */
    public String getDestinationName() {
        return destinationName;
    }

    /**
     * @param destinationName the destinationName to set
     */
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

  
    
}
