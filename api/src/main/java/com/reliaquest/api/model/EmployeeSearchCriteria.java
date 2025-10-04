package com.reliaquest.api.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSearchCriteria {

    private String id;
    private String name;
}
