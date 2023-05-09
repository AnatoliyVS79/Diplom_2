package ru.praktikum.stellarburgers.model;

public class UpdateUserRequest extends Request {
    private final String email;
    private final String name;

    public UpdateUserRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}