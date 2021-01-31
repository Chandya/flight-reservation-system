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
public class RouteIsBeingUsedException extends Exception {

    /**
     * Creates a new instance of <code>RouteIsBeingUsedException</code> without
     * detail message.
     */
    public RouteIsBeingUsedException() {
    }

    /**
     * Constructs an instance of <code>RouteIsBeingUsedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RouteIsBeingUsedException(String msg) {
        super(msg);
    }
}
