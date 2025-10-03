package com.reliaquest.api.entity.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDTO {

    private Long id;

    private String employeeId;
    private String name;

    private String email;

    private Long salary;
}
