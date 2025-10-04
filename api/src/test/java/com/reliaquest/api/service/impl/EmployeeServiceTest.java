package com.reliaquest.api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.mapper.EmployeeDtoToEmployeeEntityMapper;
import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.model.EmployeeSearchCriteria;
import com.reliaquest.api.model.entity.Employee;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EmployeeServiceTest {

    @Mock
    private EmployeeDatabaseService employeeDao;

    @Mock
    private EmployeeDtoToEmployeeEntityMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName("John Doe");
        employee.setSalary(5000);
    }

    @Test
    void testSearchEmployee() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        when(employeeDao.searchEmployee(criteria)).thenReturn(Collections.singletonList(employee));

        List<Employee> result = employeeService.searchEmployee(criteria);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(employeeDao, times(1)).searchEmployee(criteria);
    }

    @Test
    void testSearchHighestSalaryEmployeeName() {
        List<String> names = Arrays.asList("John Doe", "Jane Smith");
        when(employeeDao.findTop10EmployeesBySalary()).thenReturn(names);

        List<String> result = employeeService.searchHighestSalaryEmployeeName();

        assertEquals(2, result.size());
        assertTrue(result.contains("John Doe"));
        verify(employeeDao, times(1)).findTop10EmployeesBySalary();
    }

    @Test
    void testGetHighestSalary_WhenPresent() {
        when(employeeDao.findHighestSalary()).thenReturn(8000);

        Integer result = employeeService.getHighestSalary();

        assertEquals(8000, result);
        verify(employeeDao, times(1)).findHighestSalary();
    }

    @Test
    void testGetHighestSalary_WhenNull() {
        when(employeeDao.findHighestSalary()).thenReturn(null);

        Integer result = employeeService.getHighestSalary();

        assertEquals(0, result);
        verify(employeeDao, times(1)).findHighestSalary();
    }

    @Test
    void testCreateEmployee() {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("John Doe");
        dto.setSalary(5000);

        when(employeeMapper.mapEmployeeDtoToEmployee(dto)).thenReturn(employee);

        Employee result = employeeService.createEmployee(dto);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(employeeDao, times(1)).save(employee);
    }

    @Test
    void testDeleteEmployee_Success() {
        UUID id = UUID.randomUUID();
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName("John Doe");

        when(employeeDao.findById(id)).thenReturn(Optional.of(emp));

        String result = employeeService.deleteEmployee(id.toString());

        assertEquals("John Doe", result);
        verify(employeeDao, times(1)).delete(emp);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        UUID id = UUID.randomUUID();
        when(employeeDao.findById(id)).thenReturn(Optional.empty());

        String finalId = id.toString();
        EmployeeNotFoundException exception =
                assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(finalId));

        assertEquals("Employee not found with ID: " + finalId, exception.getMessage());
        verify(employeeDao, never()).delete(any());
    }

    @Test
    void testDeleteEmployee_InvalidUUID() {
        String invalidId = "invalid-uuid";

        assertThrows(IllegalArgumentException.class, () -> employeeService.deleteEmployee(invalidId));

        verify(employeeDao, never()).delete(any());
    }

    @Test
    void testDeleteEmployee_ExceptionHandled() {
        UUID id = UUID.randomUUID();
        when(employeeDao.findById(id)).thenThrow(new RuntimeException("DB error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> employeeService.deleteEmployee(id.toString()));

        assertEquals("DB error", ex.getMessage());
        verify(employeeDao, never()).delete(any());
    }
}
