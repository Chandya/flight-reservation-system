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
public class DeleteRouteException extends Exception {

    /**
     * Creates a new instance of <code>DeleteRouteException</code> without
     * detail message.
     */
    public DeleteRouteException() {
    }

    /**
     * Constructs an instance of <code>DeleteRouteException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteRouteException(String msg) {
        super(msg);
    }
}
