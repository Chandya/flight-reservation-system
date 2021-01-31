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
public class MaximumCapacityExceedException extends Exception {

    /**
     * Creates a new instance of <code>MaximumCapacityExceedException</code>
     * without detail message.
     */
    public MaximumCapacityExceedException() {
    }

    /**
     * Constructs an instance of <code>MaximumCapacityExceedException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public MaximumCapacityExceedException(String msg) {
        super(msg);
    }
}
