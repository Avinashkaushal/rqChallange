package com.reliaquest.api.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    private String id;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Salary must not be null")
    @Positive(message = "Salary must be greater than zero")
    private Integer salary;

    @NotNull(message = "Age must not be null")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 65, message = "Age must not exceed 65")
    private Integer age;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private String email;
}
