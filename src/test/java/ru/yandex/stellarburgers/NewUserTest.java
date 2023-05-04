package ru.yandex.stellarburgers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.stellarburgers.model.Tokens;
import ru.praktikum.stellarburgers.model.User;
import ru.praktikum.stellarburgers.model.UserLogin;
import ru.praktikum.stellarburgers.client.UserRequest;

import static org.hamcrest.CoreMatchers.equalTo;

public class NewUserTest {
    private UserRequest userRequest;
    private User user;
    private  UserLogin userLogin;

    @Before //создание случайного пользователя
    public void setUp(){
        userRequest = new UserRequest();
        user = new User().createUser();

    }

    @After //удаление созданного пользователя
    public void tearDown(){
        userLogin =new UserLogin();
        JsonElement accessTokenFull = userRequest.login(userLogin.from(user).toString()).thenReturn()
                .body().as(JsonObject.class).get("accessToken");
        if (accessTokenFull != null){
            String accessToken = accessTokenFull.toString().substring(8, 179);
            Tokens.setAccessToken(accessToken);
            userRequest.delete().then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Тест проверяет возможность создать пользователя. Запрос возвращает код ответа \"200\" " +
            "с телом ответа \" success\":true")
    public void userCanBeCreated(){
        userRequest.create(user.toString()).then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых пользователей")
    @Description("Тест проверяет появление ошибки в случае если пользователь уже существует")
    public void userCanBeNotCreateIsTwoIdenticalUser(){
        userRequest.create(user.toString()).then().statusCode(200);
        userRequest.create(user.toString()).then().assertThat()
                .statusCode(403)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без указания поля password")
    @Description("Тест проверяет появление ошибки в случае отсутствия обязательного поля password")
    public void userNotBeCreateWithoutRequiredFieldPassword(){
        User user = new User("testLogin", "", "testName");
        userRequest.create(user.toString()).then().assertThat()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без указания поля email")
    @Description("Тест проверяет появление ошибки в случае отсутствия обязательного поля email")
    public void userNotBeCreateWithoutRequiredFieldLogin() {
        User user = new User("", "testPassword", "testName");
        userRequest.create(user.toString()).then().assertThat()
                .statusCode(403)
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без указания поля name")
    @Description("Тест проверяет появление ошибки в случае отсутствия обязательного поля name")
    public void userNotBeCreateWithoutRequiredFieldName() {
        User user = new User("testUser", "testPassword", "");
        userRequest.create(user.toString()).then().assertThat()
                .statusCode(403)
                .body("message", Matchers.equalTo("Email, password and name are required fields"));
    }
}
