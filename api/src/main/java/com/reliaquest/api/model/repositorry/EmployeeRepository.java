package com.reliaquest.api.model.repositorry;

import com.reliaquest.api.model.entity.Employee;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query("SELECT e.name FROM Employee e ORDER BY e.salary DESC")
    List<String> findTop10EmployeesBySalary(Pageable pageable);

    @Query("SELECT MAX(e.salary) FROM Employee e")
    Integer findHighestSalary();
}
