package ru.praktikum.stellarburgers.model;

import org.apache.commons.lang3.RandomStringUtils;

public class RegisterUserRequest extends Request {
    private final String getEmail;
    private final String password;
    private final String name;

    public RegisterUserRequest(String email, String password, String name) {
        this.getEmail = email;
        this.password = password;
        this.name = name;
    }

    public static RegisterUserRequest getRandom() {
        final String name = RandomStringUtils.randomAlphabetic(10);
        final String email = RandomStringUtils.randomAlphabetic(10) + EMAIL_POSTFIX;
        final String password = RandomStringUtils.randomAlphabetic(10);
        return new RegisterUserRequest(email, password, name);
    }

    public String getEmail() {
        return getEmail;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}