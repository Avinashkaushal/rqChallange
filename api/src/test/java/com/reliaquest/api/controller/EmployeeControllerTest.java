package com.reliaquest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.model.entity.Employee;
import com.reliaquest.api.service.impl.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName("John Doe");
        employee.setSalary(5000);
    }

    @Test
    void testGetAllEmployees() throws Exception {
        List<Employee> employees = Collections.singletonList(employee);
        Mockito.when(employeeService.searchEmployee(any())).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetEmployeesByNameSearch() throws Exception {
        Mockito.when(employeeService.searchEmployee(any())).thenReturn(List.of(employee));

        mockMvc.perform(get("/api/v1/employees/search/{name}", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetEmployeeById() throws Exception {
        Mockito.when(employeeService.searchEmployee(any())).thenReturn(List.of(employee));

        mockMvc.perform(get("/api/v1/employees/{id}", employee.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetHighestSalaryOfEmployees() throws Exception {
        Mockito.when(employeeService.getHighestSalary()).thenReturn(8000);

        mockMvc.perform(get("/api/v1/employees/highestSalary"))
                .andExpect(status().isOk())
                .andExpect(content().string("8000"));
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        Mockito.when(employeeService.searchHighestSalaryEmployeeName()).thenReturn(Arrays.asList("Alice", "Bob"));

        mockMvc.perform(get("/api/v1/employees/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Alice"))
                .andExpect(jsonPath("$[1]").value("Bob"));
    }

    @Test
    void testCreateEmployee() throws Exception {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("Jane Doe");
        dto.setSalary(6000);
        dto.setAge(19);
        dto.setTitle("SeniorSoftwareEngineer");
        dto.setEmail("test@reliaquest.com");
        Mockito.when(employeeService.createEmployee(any(EmployeeDTO.class)))
                .thenReturn(employee);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe")); // matches mocked entity
    }

    @Test
    void testDeleteEmployeeById() throws Exception {
        Mockito.when(employeeService.deleteEmployee(eq(employee.getId().toString()))).thenReturn("John Doe");

        mockMvc.perform(delete("/api/v1/employees/{id}", employee.getId().toString()))
                .andExpect(status().isCreated())
                .andExpect(content().string("John Doe"));
    }
}
