package com.reliaquest.api.service;

import com.reliaquest.api.mapper.EmployeeDtoToEmployeeEntityMapper;
import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.model.EmployeeSearchCriteria;
import com.reliaquest.api.model.entity.Employee;
import com.reliaquest.api.model.repositorry.EmployeeRepository;
import com.reliaquest.api.util.EmployeeUtility;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EntityManager entityManager;

    private final EmployeeRepository employeeRepository;

    private final EmployeeDtoToEmployeeEntityMapper employeeDtoToEmployeeEntityMapper;

    @Override
    public List<Employee> searchEmployee(EmployeeSearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
        Root<Employee> employee = cq.from(Employee.class);
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getId() != null && !criteria.getId().isBlank()) {
            UUID uuid = EmployeeUtility.covertStringToUuid(criteria.getId());
            if (uuid != null) {
                predicates.add(cb.equal(employee.get("id"), uuid));
            }
        }

        if (criteria.getName() != null && !criteria.getName().isBlank()) {
            predicates.add(cb.like(cb.lower(employee.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<String> searchHighestSalaryEmployeeName() {
        return employeeRepository.findTop10EmployeesBySalary(PageRequest.of(0, 10));
    }

    @Override
    public Integer getHighestSalary() {
        Integer highestSalary = employeeRepository.findHighestSalary();
        return highestSalary != null ? highestSalary : 0;
    }

    @Override
    @Transactional
    public Employee createEmployee(EmployeeDTO employeeInput) {
        Employee employee = employeeDtoToEmployeeEntityMapper.mapEmployeeDtoToEmployee(employeeInput);
        entityManager.persist(employee);
        return employee;
    }

    @Override
    public String deleteEmployee(String id) {
        try {
            UUID employeeId = EmployeeUtility.covertStringToUuid(id);
            if (Objects.nonNull(employeeId)) {
                return employeeRepository.findById(employeeId)
                        .map(employee -> {
                            employeeRepository.delete(employee);
                            return employee.getName();
                        })
                        .orElse("");
            } else {
                return "";
            }
        }catch (Exception ex) {
            log.error("Failed to delete employee with id {}: {}", id, ex.getMessage(), ex);
            return "";
        }

    }
}
