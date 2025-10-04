package com.reliaquest.api.service.impl;

import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.mapper.EmployeeDtoToEmployeeEntityMapper;
import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.model.EmployeeSearchCriteria;
import com.reliaquest.api.model.entity.Employee;
import com.reliaquest.api.service.IEmployeeService;
import com.reliaquest.api.util.EmployeeUtility;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeDatabaseService employeeDao;
    private final EmployeeDtoToEmployeeEntityMapper employeeDtoToEmployeeEntityMapper;

    @Override
    public List<Employee> searchEmployee(EmployeeSearchCriteria criteria) {
        return employeeDao.searchEmployee(criteria);
    }

    @Override
    public List<String> searchHighestSalaryEmployeeName() {
        return employeeDao.findTop10EmployeesBySalary();
    }

    @Override
    public Integer getHighestSalary() {
        return Optional.ofNullable(employeeDao.findHighestSalary()).orElse(0);
    }

    /**
     * Creates a new employee record in the system.
     * <p>
     * This method maps the given {@link EmployeeDTO} object to an {@link Employee}
     * entity using {@link EmployeeDtoToEmployeeEntityMapper}, persists it to the
     * database, and returns the saved entity.
     * </p>
     * @param employeeInput the employee details provided as a {@link EmployeeDTO}
     * @return the persisted {@link Employee} entity with any generated fields
     *         (such as ID) populated
     */
    @Override
    @Transactional
    public Employee createEmployee(EmployeeDTO employeeInput) {
        Employee employee = employeeDtoToEmployeeEntityMapper.mapEmployeeDtoToEmployee(employeeInput);
        employeeDao.save(employee);
        return employee;
    }

    /**
     * Deletes an employee by their identifier.
     * <p>
     * This method attempts to convert the provided employee ID string into a {@link UUID}.
     * If the ID is valid and an employee with that ID exists, the employee record is
     * deleted from the database and the employee's name is returned.
     * </p>
     *
     * <p>
     * If the employee ID is invalid, no employee is found, or an exception occurs during
     * the operation, the method will return an empty string.
     * </p>
     *
     * <p><b>Error Handling:</b> Any exceptions are logged, but not propagated, to ensure
     * that the service continues running without interruption.</p>
     *
     * @param id the employee ID as a string (expected to be a valid UUID)
     * @return the name of the deleted employee if successful, or an empty string if
     *         the employee does not exist, the ID is invalid, or an error occurs
     */
    @Override
    public String deleteEmployee(String id) {
        UUID employeeId = EmployeeUtility.covertStringToUuid(id);
        if (employeeId == null) {
            throw new IllegalArgumentException("Invalid employee ID: " + id);
        }

        return employeeDao
                .findById(employeeId)
                .map(employee -> {
                    employeeDao.delete(employee);
                    return employee.getName();
                })
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
    }
}
