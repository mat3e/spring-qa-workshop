package io.github.mat3e.downloads.exceptionhandling;

import java.util.Locale;

public class BusinessException extends RuntimeException {
    public static BusinessException notFound(String entityName, String id) {
        return new EntityNotFoundException(entityName, id);
    }

    public BusinessException(String message) {
        super(message);
    }
}

class EntityNotFoundException extends BusinessException {
    EntityNotFoundException(String entityName, String id) {
        super(capitalize(entityName) + " with id " + id + " not found");
    }

    private static String capitalize(String entityName) {
        return entityName.substring(0, 1).toUpperCase(Locale.ENGLISH) + entityName.substring(1).toLowerCase(Locale.ENGLISH);
    }
}
