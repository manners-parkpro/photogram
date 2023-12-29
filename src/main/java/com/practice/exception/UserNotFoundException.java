package com.practice.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends NotFoundException {
    private String userName;

    public static final int MODIFY_USER_NOT_FOUND = 101;

    public UserNotFoundException(String userName) {
        super("User not found: " + userName, USER_NOT_FOUND);
        this.userName = userName;
    }

    public UserNotFoundException(String userName, int code) {
        super("User not found: " + userName, code);
        this.userName = userName;
    }
}
