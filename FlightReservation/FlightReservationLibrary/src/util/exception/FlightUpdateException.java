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
public class FlightUpdateException extends Exception {

    /**
     * Creates a new instance of <code>FlightUpdateException</code> without
     * detail message.
     */
    public FlightUpdateException() {
    }

    /**
     * Constructs an instance of <code>FlightUpdateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FlightUpdateException(String msg) {
        super(msg);
    }
}
