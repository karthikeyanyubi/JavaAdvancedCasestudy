package com.employee_service.model;


import com.employee_service.validation.DateFormatConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Table(name = "employee")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "first_name")
    @NotBlank(message = "First Name should be provided")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last Name should be provided")
    private String lastName;

    @Column(name = "dob")
    @DateFormatConstraint
    private LocalDate dateOfBirth;

    @Column(name = "doj")
    @NotNull(message = "Date of Joining should be provided")
    @DateFormatConstraint
    private LocalDate dateOfJoining;

    @Pattern(regexp = "M[1-6]", message = "Grade must be in the format M1 to M6")
    @Column(name = "grade")
    private String grade;

}
