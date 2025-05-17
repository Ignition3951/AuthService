package com.utk.authservice.exception;

public class UserDetailsNotValid extends RuntimeException{

    public UserDetailsNotValid(String message) {
        super(message);
    }
}
