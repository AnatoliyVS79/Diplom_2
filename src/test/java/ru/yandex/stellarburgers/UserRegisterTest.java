package ru.yandex.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.stellarburgers.client.UserClient;
import ru.praktikum.stellarburgers.model.RegisterUserRequest;

public class UserRegisterTest {
    UserClient userClient;
    ValidatableResponse validatableResponse;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (validatableResponse != null) {
            userClient.saveUserToken(validatableResponse);
        }
        userClient.deleteUserAndFlushToken();
    }

    @Test
    @DisplayName("Создаем уникального пользователя")
    @Description("Проверяем что возвращается ответ true и статус код 200")
    public void testRegisterNewUserReturn200True() {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.getRandom();
        validatableResponse = userClient.registerNewUserAndReturnResponse(registerUserRequest);

        validatableResponse
                .assertThat().body("success", Matchers.equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Создаем пользователя уже зарегистрированного в системе")
    @Description("Проверяем что возвращается ответ false и статус код 403")
    public void testRegisterExistingUserReturn403() {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.getRandom();
        validatableResponse = userClient.registerNewUserAndReturnResponse(registerUserRequest);
        userClient.registerNewUserAndReturnResponse(registerUserRequest)
                .assertThat().body("success", Matchers.equalTo(false),
                        "message", Matchers.equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создаем пользователя с незаполненным полем email")
    @Description("Проверяем что возвращается ответ false и статус код 403")
    public void testRegisterUserWithNoEmailReturn403() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(null, "password", "name");
        userClient.registerNewUserAndReturnResponse(registerUserRequest)
                .assertThat().body("success", Matchers.equalTo(false),
                        "message", Matchers.equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создаем пользователя с незаполненным полем name")
    @Description("Проверяем что возвращается ответ false и статус код 403")
    public void testRegisterUserWithNoNameReturn403() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest("email@yandex.ru", "password", null);
        userClient.registerNewUserAndReturnResponse(registerUserRequest)
                .assertThat().body("success", Matchers.equalTo(false),
                        "message", Matchers.equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Создаем пользователя с незаполненным полем password")
    @Description("Проверяем что возвращается ответ false и статус код 403")
    public void testRegisterUserWithNoPasswordReturn403() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest("email@yandex.ru", null, "name");
        userClient.registerNewUserAndReturnResponse(registerUserRequest)
                .assertThat().body("success", Matchers.equalTo(false),
                        "message", Matchers.equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
}
