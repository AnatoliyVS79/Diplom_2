package ru.yandex.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.stellarburgers.client.UserClient;
import ru.praktikum.stellarburgers.model.LoginUserRequest;
import ru.praktikum.stellarburgers.model.RegisterUserRequest;

public class LoginUserTest {
    UserClient userClient;
    RegisterUserRequest registerUserRequest;

    @Before
    public void setUp() {
        userClient = new UserClient();
        registerUserRequest = RegisterUserRequest.getRandom();
        userClient.saveUserToken(userClient.registerNewUserAndReturnResponse(registerUserRequest));
    }

    @After
    public void tearDown() {
        userClient.deleteUserAndFlushToken();
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Проверяем что возвращается ответ true и статус код 200")
    public void testLoginUserReturn200True() {
        LoginUserRequest loginUserRequest = new LoginUserRequest(registerUserRequest.getEmail(),
                registerUserRequest.getPassword());
        userClient.loginUserAndReturnResponse(loginUserRequest)
                .assertThat().body("success", Matchers.equalTo(true))
                .and()
                .statusCode(200);
    }


    @Test
    @DisplayName("Авторизуем пользователя с неверным паролем")
    @Description("Проверяем что возвращается ответ false и статус код 401")
    public void testLoginUserWithWrongPasswordReturn401() {
        LoginUserRequest loginUserRequest = new LoginUserRequest(registerUserRequest.getEmail(),
                registerUserRequest.getPassword() + "_wrong");
        userClient.loginUserAndReturnResponse(loginUserRequest)
                .assertThat().body("success", Matchers.equalTo(false),
                        "message", Matchers.equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Авторизуем пользователя с неверной почтой")
    @Description("Проверяем что в отнвете  возвращается false и статус код 401")
    public void testLoginUserWithWrongEmailReturn401() {

        LoginUserRequest loginUserRequest = new LoginUserRequest(registerUserRequest.getEmail() + " wrong",
                registerUserRequest.getPassword());
        userClient.loginUserAndReturnResponse(loginUserRequest)
                .assertThat().body("success", Matchers.equalTo(false),
                        "message", Matchers.equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Авторизуем незарегистрированного пользователя")
    @Description("Проверяем что в отнвете  возвращается false и статус код 401")
    public void testLoginUserWithWrongEmailAndPasswordReturn401() {
        userClient.deleteUserAndFlushToken();

        LoginUserRequest loginUserRequest = new LoginUserRequest(registerUserRequest.getEmail(),
                registerUserRequest.getPassword() + "_wrong");
        userClient.loginUserAndReturnResponse(loginUserRequest)
                .assertThat().body("success", Matchers.equalTo(false),
                        "message", Matchers.equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
}




