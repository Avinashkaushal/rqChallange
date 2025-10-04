package com.reliaquest.api.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.model.EmployeeSearchCriteria;
import com.reliaquest.api.model.entity.Employee;
import com.reliaquest.api.model.repositorry.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class EmployeeDbServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private CriteriaQuery<Employee> cq;

    @Mock
    private Root<Employee> root;

    @Mock
    private TypedQuery<Employee> typedQuery;

    @InjectMocks
    private EmployeeDatabaseService employeeDatabaseService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setName("John Doe");
    }

    @Test
    void testSearchEmployee_ById() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setId(UUID.randomUUID().toString());

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Employee.class)).thenReturn(cq);
        when(cq.from(Employee.class)).thenReturn(root);
        when(entityManager.createQuery(cq)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(employee));

        List<Employee> result = employeeDatabaseService.searchEmployee(criteria);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testSearchEmployee_ByName() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setName("John");

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Employee.class)).thenReturn(cq);
        when(cq.from(Employee.class)).thenReturn(root);
        when(entityManager.createQuery(cq)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(employee));

        List<Employee> result = employeeDatabaseService.searchEmployee(criteria);

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testSearchEmployee_NoCriteria() {
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();

        when(entityManager.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Employee.class)).thenReturn(cq);
        when(cq.from(Employee.class)).thenReturn(root);
        when(entityManager.createQuery(cq)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(employee, employee));

        List<Employee> result = employeeDatabaseService.searchEmployee(criteria);

        assertEquals(2, result.size());
    }

    @Test
    void testFindTop10EmployeesBySalary() {
        List<String> names = Arrays.asList("John", "Jane");
        when(employeeRepository.findTop10EmployeesBySalary(PageRequest.of(0, 10)))
                .thenReturn(names);

        List<String> result = employeeDatabaseService.findTop10EmployeesBySalary();

        assertEquals(2, result.size());
        assertTrue(result.contains("John"));
    }

    @Test
    void testFindHighestSalary() {
        when(employeeRepository.findHighestSalary()).thenReturn(1000);

        Integer salary = employeeDatabaseService.findHighestSalary();

        assertEquals(1000, salary);
    }

    @Test
    void testSave() {
        employeeDatabaseService.save(employee);
        verify(entityManager, times(1)).persist(employee);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeDatabaseService.findById(id);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testDelete() {
        employeeDatabaseService.delete(employee);
        verify(employeeRepository, times(1)).delete(employee);
    }
}
