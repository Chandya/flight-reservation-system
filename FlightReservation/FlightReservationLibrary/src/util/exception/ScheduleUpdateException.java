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
public class ScheduleUpdateException extends Exception {

    /**
     * Creates a new instance of <code>ScheduleUpdateException</code> without
     * detail message.
     */
    public ScheduleUpdateException() {
    }

    /**
     * Constructs an instance of <code>ScheduleUpdateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ScheduleUpdateException(String msg) {
        super(msg);
    }
}
