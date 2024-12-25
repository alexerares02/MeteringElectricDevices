package com.programming.user_service.controller.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(LoginFailed.class)
    public ResponseEntity<String> handleLoginFailed(LoginFailed ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

}
