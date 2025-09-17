package org.codedex.exeption;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationErrors(MethodArgumentNotValidException ex){
        return ResponseEntity.badRequest().body(ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList()
        );
    }

    //catch enum related validation errors WIP
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleValidationErrors(HttpMessageNotReadableException ex){
        return ResponseEntity.badRequest().body("Invalid request body: "+ ex.getMostSpecificCause().getMessage());
    }
}
