package com.employee_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateFormatValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormatConstraint {
    String message() default "Invalid date format. Date should be in mm/dd/yyyy format.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
