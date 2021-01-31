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
public class InvalidUserRoleException extends Exception {

    /**
     * Creates a new instance of <code>InvalidUserRoleException</code> without
     * detail message.
     */
    public InvalidUserRoleException() {
    }

    /**
     * Constructs an instance of <code>InvalidUserRoleException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidUserRoleException(String msg) {
        super(msg);
    }
}
