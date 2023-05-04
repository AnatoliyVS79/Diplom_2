package ru.praktikum.stellarburgers.model;

import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private String login;
    private String password;
    private String name;

    public User(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public User() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User createUser() {
        // с помощью библиотеки RandomStringUtils генерируем логин
        String userLogin = RandomStringUtils.randomAlphabetic(7) + "@yandex.ru";
        // с помощью библиотеки RandomStringUtils генерируем пароль
        String userPassword = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя пользователя
        String userName = RandomStringUtils.randomAlphabetic(10);

        return new User(userLogin, userPassword, userName);
    }

    @Override
    public String toString() {
        return "{\"" +
                "email\":\"" + login + "\",\"" +
                "password\":\"" + password + "\",\"" +
                "name\":\"" + name + "\"}";
    }
}
