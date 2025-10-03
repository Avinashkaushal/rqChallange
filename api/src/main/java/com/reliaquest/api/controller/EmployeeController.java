package com.reliaquest.api.controller;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.entity.model.EmployeeDTO;
import com.reliaquest.api.entity.model.EmployeeSearchCriteria;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController implements IEmployeeController<Employee, EmployeeDTO> {

    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employeesList = employeeService.searchEmployee(new EmployeeSearchCriteria());
        return new ResponseEntity<>(employeesList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        List<Employee> employeesList = employeeService.searchEmployee(EmployeeSearchCriteria.builder().name(searchString).build());
        return new ResponseEntity<>(employeesList,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        List<Employee> employeesList = employeeService.searchEmployee(EmployeeSearchCriteria.builder().employeeId(id).build());
        return new ResponseEntity<>(Optional.of(employeesList.get(0)).orElse(new Employee()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        Integer highestSalary = employeeService.getHighestSalary();
        return new ResponseEntity<>(highestSalary,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<String> employeeNames = employeeService.searchHighestSalaryEmployeeName();
        return new ResponseEntity<>(employeeNames,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(EmployeeDTO employeeInput) {
        return new ResponseEntity<>(employeeService.createEmployee(),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return new ResponseEntity<>(employeeService.deleteEmployee(),HttpStatus.CREATED);
    }
}
