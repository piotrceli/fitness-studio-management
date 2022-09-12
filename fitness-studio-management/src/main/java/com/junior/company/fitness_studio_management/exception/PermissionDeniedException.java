package com.junior.company.fitness_studio_management.exception;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException(String message) {
        super(message);
    }
}
