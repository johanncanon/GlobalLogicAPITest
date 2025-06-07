package com.johanncanon.globallogic.user_management_service.exception.custom;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
