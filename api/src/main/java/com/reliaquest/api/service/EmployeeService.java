package com.reliaquest.api.service;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.entity.model.EmployeeSearchCriteria;
import java.util.List;

public interface EmployeeService {

    List<Employee> searchEmployee(EmployeeSearchCriteria searchCriteria);
    List<String> searchHighestSalaryEmployeeName();
    Integer  getHighestSalary();
    Employee createEmployee();
    String deleteEmployee();
}
