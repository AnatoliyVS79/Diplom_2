package ru.praktikum.stellarburgers.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.stellarburgers.model.Tokens;
import ru.praktikum.stellarburgers.model.User;
import ru.praktikum.stellarburgers.model.UserLogin;

import static io.restassured.RestAssured.given;

public class UserRequest extends BaseData {
    public  final String USER_PATH = "api/auth/";

    @Step("Создание пользователя {user}")
    public  Response create(String user){
        return given()
                .spec(getBaseSpec())
                .body(user)
                .post(USER_PATH + "register/");
    }

    @Step("Вход в систему {userLogin}")
    public  Response login(String userLogin){
        return given()
                .spec(getBaseSpec())
                .body(userLogin)
                .post(USER_PATH + "login/");
    }

    @Step("Изменение данных пользователя {userLogin}")
    public Response change(String changeData){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .body(changeData)
                .patch(USER_PATH + "user/");
    }

    @Step("Изменение данных неавторизованного пользователя")
    public Response changeUnauthorized(String changeData){
        return given()
                .spec(getBaseSpec())
                .body(changeData)
                .patch(USER_PATH + "user/");
    }

    @Step("Удаление пользователя")
    public Response delete(){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(Tokens.getAccessToken())
                .when()
                .delete(USER_PATH + "user/");
    }

    @Step("Сохранение пароля")
    public void saveToken(UserRequest userRequest, UserLogin userLogin, User user) {
        JsonElement accessTokenFull = userRequest.login(userLogin.from(user).toString()).thenReturn()
                .body().as(JsonObject.class).get("accessToken");
        String accessToken = accessTokenFull.toString().substring(8, 179);
        Tokens.setAccessToken(accessToken);
    }
}
