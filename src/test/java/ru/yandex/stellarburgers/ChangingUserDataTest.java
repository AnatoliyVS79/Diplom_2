package ru.yandex.stellarburgers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.stellarburgers.client.UserRequest;
import ru.praktikum.stellarburgers.model.Tokens;
import ru.praktikum.stellarburgers.model.User;
import ru.praktikum.stellarburgers.model.UserLogin;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangingUserDataTest {
    private UserRequest userRequest;
    private User user;
    private UserLogin userLogin;

    @Before //создаем случайного пользователя
    public void setUp() {
        userRequest = new UserRequest();
        user = new User().createUser();
        userRequest.create(user.toString());
        userLogin = new UserLogin();
        userRequest.saveToken(userRequest, userLogin, user);
    }

    @After //удаляем созданного пользователя
    public void teamDown() {
        if (userRequest != null) {
            userRequest.delete().then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Изменение email авторизированного пользователя")
    @Description("Тест проверяет возможность изменения email авторизированного пользователя")
    public void userChangeEmail() {
        String changeData = "{\"" + "email" + "\":" + "\"" + RandomStringUtils.randomAlphabetic(3) +
                user.getLogin() + "\"}";
        userRequest.change(changeData).then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение пароля авторизированного пользователя")
    @Description("Тест проверяет возможность изменения пароля авторизированного пользователя")
    public void userChangePassword() {
        String changeData = "{\"" + "password" + "\":" + "\"" + RandomStringUtils.randomAlphabetic(3) +
                user.getPassword() + "\"}";
        userRequest.change(changeData).then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение имени авторизированного пользователя")
    @Description("Тест проверяет возможность изменения имени авторизированного пользователя")
    public void userChangeName() {
        String changeData = "{\"" + "name" + "\":" + "\"" + RandomStringUtils.randomAlphabetic(3) + user.getName() + "\"}";
        userRequest.change(changeData).then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Изменение email неавторизированного пользователя")
    @Description("Тест проверяет появление ошибки при попытке изменения email неавторизированного пользователя")
    public void changeTheEmailOfAnUnauthorizedUser() {
        String changeData = "{\"" + "email" + "\":" + "\"" + RandomStringUtils.randomAlphabetic(3) + user.getLogin() + "\"}";
        userRequest.changeUnauthorized(changeData).then().assertThat()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение пароля неавторизированного пользователя")
    @Description("Тест проверяет появление ошибки при попытке изменения пароля неавторизированного пользователя")
    public void changeThePasswordOfAnUnauthorizedUser() {
        String changeData = "{\"" + "password" + "\":" + "\"" + RandomStringUtils.randomAlphabetic(3) + user.getPassword() + "\"}";
        userRequest.changeUnauthorized(changeData).then().assertThat()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение имени неавторизированного пользователя")
    @Description("Тест проверяет появление ошибки при попытке изменения имени неавторизированного пользователя")
    public void changeTheNameOfAnUnauthorizedUser() {
        String changeData = "{\"" + "name" + "\":" + "\"" + RandomStringUtils.randomAlphabetic(3) + user.getName() + "\"}";
        userRequest.changeUnauthorized(changeData).then().assertThat()
                .statusCode(401)
                .body("message", equalTo("You should be authorised"));
    }
}
