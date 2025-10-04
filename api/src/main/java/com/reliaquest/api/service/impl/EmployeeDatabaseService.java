package com.reliaquest.api.service.impl;

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
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeDatabaseService {

    // For criteria builder.
    private final EntityManager entityManager;

    // For jpa derived queries.
    private final EmployeeRepository employeeRepository;

    /**
     * Searches for employees based on the given search criteria.
     * <p>
     * This method builds a dynamic JPA Criteria query using the provided
     * {@link EmployeeSearchCriteria}.
     * @param criteria the {@link EmployeeSearchCriteria} containing optional filters
     * @return a list of {@link Employee} objects matching the search criteria,
     *         or all employees if no criteria are specified
     */
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
            predicates.add(cb.like(
                    cb.lower(employee.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(cq).getResultList();
    }

    /*We can make this request pageable in order to get the highest salary of as
       many employees as we need hence using page size: 10.
     */
    public List<String> findTop10EmployeesBySalary() {
        return employeeRepository.findTop10EmployeesBySalary(PageRequest.of(0, 10));
    }

    public Integer findHighestSalary() {
        return employeeRepository.findHighestSalary();
    }

    public void save(Employee employee) {
        entityManager.persist(employee);
    }

    public Optional<Employee> findById(UUID id) {
        return employeeRepository.findById(id);
    }

    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }
}
