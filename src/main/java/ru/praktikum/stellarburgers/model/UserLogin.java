package ru.praktikum.stellarburgers.model;

public class UserLogin {
    public String login;
    public String password;

    public UserLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static UserLogin from(User user){
        return new UserLogin(user.login, user.password);
    }

    @Override
    public String toString(){
        return "{\"" +
                "email\":\"" + login + "\",\"" +
                "password\":\"" + password + "\"}";
    }
}
