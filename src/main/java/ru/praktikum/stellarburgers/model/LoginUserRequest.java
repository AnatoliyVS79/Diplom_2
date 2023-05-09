package ru.praktikum.stellarburgers.model;

public class LoginUserRequest extends Request {
    private final String email;
    private final String password;

    public LoginUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}