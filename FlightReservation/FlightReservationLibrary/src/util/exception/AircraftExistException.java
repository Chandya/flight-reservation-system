/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author chandya
 */
public class AircraftExistException extends Exception {

    /**
     * Creates a new instance of <code>AircraftExistException</code> without
     * detail message.
     */
    public AircraftExistException() {
    }

    /**
     * Constructs an instance of <code>AircraftExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AircraftExistException(String msg) {
        super(msg);
    }
}
