package com.programming.user_service.controller.handlers;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;

public class LoginFailed extends CustomException {
    private static final String MESSAGE = "Wrong credentials";
    private static final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public LoginFailed(String resource) {
        super(MESSAGE,httpStatus, resource, new ArrayList<>());
    }
}
