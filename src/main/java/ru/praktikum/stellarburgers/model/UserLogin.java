package ru.praktikum.stellarburgers.model;

public class UserLogin {
    private  String login;
    private  String password;

    public UserLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserLogin() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserLogin from(User user){
        return new UserLogin(user.getLogin(), user.getPassword());
    }

    @Override
    public String toString(){
        return "{\"" +
                "email\":\"" + login + "\",\"" +
                "password\":\"" + password + "\"}";
    }
}
