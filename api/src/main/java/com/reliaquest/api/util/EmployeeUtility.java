package com.reliaquest.api.util;

import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class EmployeeUtility {

    public static UUID covertStringToUuid(String employeeId) {
        try {
           return UUID.fromString(employeeId);
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }
}
