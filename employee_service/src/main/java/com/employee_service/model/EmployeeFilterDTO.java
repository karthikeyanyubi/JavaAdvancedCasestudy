package com.employee_service.model;


import com.employee_service.validation.DateFormatConstraint;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFilterDTO {

    private Integer employeeId;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    @Pattern(regexp = "^(lt|gt|eq)$", message = "Invalid value for dobCondition")
    private String dobCondition;

    private LocalDate dateOfJoining;

    @Pattern(regexp = "^(lt|gt|eq)$", message = "Invalid value for dojCondition")
    private String dojCondition;

    @Pattern(regexp = "M[1-6]", message = "Grade must be in the format M1 to M6")
    private String grade;

}

