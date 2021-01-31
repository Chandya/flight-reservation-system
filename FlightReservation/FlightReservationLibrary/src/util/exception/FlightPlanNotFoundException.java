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
public class FlightPlanNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>FlightPlanNotFoundException</code>
     * without detail message.
     */
    public FlightPlanNotFoundException() {
    }

    /**
     * Constructs an instance of <code>FlightPlanNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightPlanNotFoundException(String msg) {
        super(msg);
    }
}
