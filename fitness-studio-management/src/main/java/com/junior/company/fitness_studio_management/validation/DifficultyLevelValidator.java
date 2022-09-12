package com.junior.company.fitness_studio_management.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DifficultyLevelValidator implements ConstraintValidator<ValidDifficultyLevel, String> {

    private static final String DIFFICULTY_LEVEL_PATTERN = "BEGINNER|INTERMEDIATE|ADVANCED";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile(DIFFICULTY_LEVEL_PATTERN);
        if (value == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
