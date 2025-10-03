package com.reliaquest.api.entity.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSearchCriteria {

    private Long id;
    private String employeeId;
    private String name;
}
