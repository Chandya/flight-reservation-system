/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Jarryl Yeo
 */
public class FlightPlanUpdateException extends Exception {

    /**
     * Creates a new instance of <code>FlightPlanUpdateException</code> without
     * detail message.
     */
    public FlightPlanUpdateException() {
    }

    /**
     * Constructs an instance of <code>FlightPlanUpdateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightPlanUpdateException(String msg) {
        super(msg);
    }
}
