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
public class FlightPlanExistsException extends Exception {

    /**
     * Creates a new instance of <code>FlightPlanExistsException</code> without
     * detail message.
     */
    public FlightPlanExistsException() {
    }

    /**
     * Constructs an instance of <code>FlightPlanExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightPlanExistsException(String msg) {
        super(msg);
    }
}
