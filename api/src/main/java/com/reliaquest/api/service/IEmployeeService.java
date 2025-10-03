package com.reliaquest.api.service;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.entity.model.EmployeeSearchCriteria;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IEmployeeService implements EmployeeService {

    @Override
    public List<Employee> searchEmployee(EmployeeSearchCriteria searchCriteria) {
        return List.of();
    }

    @Override
    public List<String> searchHighestSalaryEmployeeName() {
        return List.of();
    }

    @Override
    public Integer getHighestSalary() {
        return 0;
    }

    @Override
    public Employee createEmployee() {
        return null;
    }

    @Override
    public String deleteEmployee() {
        return "";
    }
}
