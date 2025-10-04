package com.reliaquest.api.mapper;

import com.reliaquest.api.model.EmployeeDTO;
import com.reliaquest.api.model.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeDtoToEmployeeEntityMapper {

    Employee mapEmployeeDtoToEmployee(EmployeeDTO employeeDTO);
}
