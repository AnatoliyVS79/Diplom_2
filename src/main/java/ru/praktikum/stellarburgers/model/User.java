package ru.praktikum.stellarburgers.model;

import org.apache.commons.lang3.RandomStringUtils;

public class User {
    public String login;
    public String password;
    public String name;

    public User(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }

    public static User createUser(){
        // с помощью библиотеки RandomStringUtils генерируем логин
        String userLogin = RandomStringUtils.randomAlphabetic(7) + "@yandex.ru";
        // с помощью библиотеки RandomStringUtils генерируем пароль
        String userPassword = RandomStringUtils.randomAlphabetic(10);
        // с помощью библиотеки RandomStringUtils генерируем имя пользователя
        String userName = RandomStringUtils.randomAlphabetic(10);

        return  new User(userLogin, userPassword, userName);
    }

    @Override
    public  String toString(){
        return "{\"" +
                "email\":\"" + login + "\",\"" +
                "password\":\"" + password + "\",\"" +
                "name\":\"" + name +"\"}";
    }
}
