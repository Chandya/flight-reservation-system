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
public class AircraftCapacityInvalidException extends Exception {

    /**
     * Creates a new instance of <code>AircraftCapacityInvalidException</code>
     * without detail message.
     */
    public AircraftCapacityInvalidException() {
    }

    /**
     * Constructs an instance of <code>AircraftCapacityInvalidException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AircraftCapacityInvalidException(String msg) {
        super(msg);
    }
}
