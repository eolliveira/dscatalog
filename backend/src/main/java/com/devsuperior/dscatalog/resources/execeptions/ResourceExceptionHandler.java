package com.devsuperior.dscatalog.resources.execeptions;

import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public StandardError entityNotFound() {
        StandardError standardError = new StandardError();

    }

}
