package com.junior.company.fitness_studio_management.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DifficultyLevelValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDifficultyLevel {

    String message() default "Invalid Difficulty Level";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
