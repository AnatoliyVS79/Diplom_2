package ru.praktikum.stellarburgers.model;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static ru.praktikum.stellarburgers.model.BaseData.BASE_URL;
import static ru.praktikum.stellarburgers.model.BaseData.getBaseSpec;

public class UserRequest {
    public static final String USER_PATH = BASE_URL + "auth/";

    @Step("Создание пользователя {user}")
    public static Response create(String user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .post(USER_PATH + "register/");
    }

    @Step("Регистрация пользователя {userLogin}")
    public static Response login(String userLogin){
        return given()
                .spec(getBaseSpec())
                .body(userLogin)
                .post(USER_PATH + "login/");
    }

    @Step("Изменение данных пользователя {userLogin}")
    public static Response change(String changeData){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(Token.getAccessToken())
                .body(changeData)
                .patch(USER_PATH + "user/");
    }

    @Step("Изменение данных неавторизованного пользователя")
    public static Response changeUnauthorized(String changeData){
        return given()
                .spec(getBaseSpec())
                .body(changeData)
                .patch(USER_PATH + "user/");
    }

    @Step("Удаление пользователя")
    public static Response delete(){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(Token.getAccessToken())
                .when()
                .delete(USER_PATH + "user/");
    }
}
