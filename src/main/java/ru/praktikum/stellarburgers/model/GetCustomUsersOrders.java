package ru.praktikum.stellarburgers.model;

public class GetCustomUsersOrders extends Request {
    private final String email;

    public GetCustomUsersOrders(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}