/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Jarryl Yeo
 */
@Remote
public interface EmployeeSessionBeanRemote {
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;

    public Long createNewEmployee(Employee newEmployee) throws EmployeeUsernameExistException, UnknownPersistenceException, InputDataValidationException;
}
