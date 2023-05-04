package ru.yandex.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.stellarburgers.client.Order;
import ru.praktikum.stellarburgers.client.UserRequest;
import ru.praktikum.stellarburgers.model.User;
import ru.praktikum.stellarburgers.model.UserLogin;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserOrderTest {
    private UserRequest userRequest;
    private User user;
    private Order order;
    String accessToken;
    private UserLogin userLogin;

    @Before //создаем случайного пользователя для возможности авторизированного запроса
    public void setUp() {
        order = new Order();
        userRequest = new UserRequest();
        user = new User().createUser();
        userRequest.create(user.toString());
        userLogin = new UserLogin();
        userRequest.saveToken(userRequest, userLogin, user);
    }

    @After //удаляем созданного пользователя
    public void tearDown() {
        if (accessToken != null) {
            userRequest.delete().then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Вывод списка заказов авторизованного пользователя")
    @Description("Тест проверяет, что запрос положительный, список заказов пользователя не пустой")
    public void getAuthorizedUserOrderTest() {
        order.getUserOrderAuthorized().then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Вывод списка заказов неавторизованного пользователя")
    @Description("Тест проверяет вывод ошибки и сообщение о необходимости авторизации при запросе списка заказов " +
            "неавторизованного пользователя")
    public void getUnauthorizedUserOrderTest() {
        order.getUserOrderUnauthorized().then().assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
