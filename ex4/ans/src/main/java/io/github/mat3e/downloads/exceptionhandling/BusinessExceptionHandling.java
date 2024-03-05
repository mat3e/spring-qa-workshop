package io.github.mat3e.downloads.exceptionhandling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
class BusinessExceptionHandling extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<Void> handleNotFound(EntityNotFoundException ignored) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(BusinessException.class)
    ResponseEntity<String> handleAll(BusinessException businessException) {
        return ResponseEntity.badRequest().body(businessException.getMessage());
    }
}
