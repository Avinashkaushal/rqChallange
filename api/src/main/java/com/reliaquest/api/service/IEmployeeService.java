package com.reliaquest.api.service;

import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.model.EmployeeSearchCriteria;
import com.reliaquest.api.model.entity.Employee;
import java.util.List;

public interface IEmployeeService {

    List<Employee> searchEmployee(EmployeeSearchCriteria searchCriteria);

    List<String> searchHighestSalaryEmployeeName();

    Integer getHighestSalary();

    Employee createEmployee(EmployeeDTO employeeInput);

    String deleteEmployee(String id);
}
