package ru.yandex.stellarburgers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.stellarburgers.model.Tokens;
import ru.praktikum.stellarburgers.model.User;
import ru.praktikum.stellarburgers.model.UserLogin;
import ru.praktikum.stellarburgers.client.UserRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest {
    private UserRequest userRequest;
    private User user;
    private UserLogin userLogin;

    @Before //создаем случайного пользователя
    public void setUp() {
        userRequest = new UserRequest();
        user =new  User().createUser();
        userLogin = new UserLogin();
        userRequest.create(user.toString());
        userRequest.saveToken(userRequest, userLogin, user);
    }

    @After //удаляем созданного пользователя
    public void tearDown() {
        JsonElement accessTokenFull = userRequest.login(userLogin.from(user).toString()).thenReturn()
                .body().as(JsonObject.class).get("accessToken");
        String accessToken = accessTokenFull.toString().substring(8, 179);
        if (accessTokenFull != null) {
            Tokens.setAccessToken(accessToken);
            userRequest.delete().then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Тест проверяет возможность авторизации пользователя и вывода в случае успеха accessToken пользователя")
    public void userCanCreatedAndBeLogIn(){
        userRequest.login(userLogin.from(user).toString()).then().assertThat()
                .statusCode(200)
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Авторизация пользователя без пароля")
    @Description("Тест проверяет появление ошибки в случае указания не всех обязательных полей, без поля password")
    public void userLoginWithoutRequiredFieldPassword(){
        UserLogin userLogin = new UserLogin(user.getLogin(), "");
        userRequest.login(userLogin.toString()).then().assertThat()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя без логина")
    @Description("Тест проверяет появление ошибки в случае указания не всех обязательных полей, без поля email")
    public void userLoginWithoutRequiredFieldEmail(){
        UserLogin userLogin = new UserLogin("", user.getPassword());
        userRequest.login(userLogin.toString()).then().assertThat()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Тест проверяет появление ошибки в случае указания неверных регистрационных данных," +
            " в частности несуществующего email пользователя")
    public void courierIdIncorrectFieldEmail(){
        UserLogin userLogin = new UserLogin("123" + user.getLogin(), user.getPassword());
        userRequest.login(userLogin.toString()).then().assertThat()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Тест проверяет появление ошибки в случае указания неверных регистрационных данных, " +
            "в частности несуществующего password пользователя")
    public void courierIdIncorrectFieldPassword(){
        UserLogin userLogin = new UserLogin(user.getLogin(), user.getPassword() + "123");
        userRequest.login(userLogin.toString()).then().assertThat()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

}




