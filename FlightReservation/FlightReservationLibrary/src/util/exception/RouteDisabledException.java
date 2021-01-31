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
public class RouteDisabledException extends Exception {

    /**
     * Creates a new instance of <code>RouteDisabledException</code> without
     * detail message.
     */
    public RouteDisabledException() {
    }

    /**
     * Constructs an instance of <code>RouteDisabledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RouteDisabledException(String msg) {
        super(msg);
    }
}
