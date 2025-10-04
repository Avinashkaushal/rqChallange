package com.reliaquest.api.util;

import java.util.UUID;
import org.springframework.context.annotation.Configuration;

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
