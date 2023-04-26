package ru.yandex.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.stellarburgers.client.IngredientList;
import ru.praktikum.stellarburgers.client.Order;
import ru.praktikum.stellarburgers.model.Tokens;
import ru.praktikum.stellarburgers.model.User;
import ru.praktikum.stellarburgers.model.UserRequest;

import static org.hamcrest.CoreMatchers.*;

public class CreateOrderTest {
    private UserRequest userRequest;
    private User user;
    private Order order;
    String accessToken;

    @Before  //создаем случайного пользователя для возможности авторизированного запроса
    public void setUp(){
        order = new Order();
        userRequest = new UserRequest();
        user = User.createUser();
        String accessTokenFull = userRequest.create(user.toString()).then().extract().body().path("accessToken");
        accessToken = accessTokenFull.substring(7,178);
        if (accessToken != null){
            Tokens.setAccessToken(accessToken);
        }
    }

    @After  //удаляем созданного пользователя
    public void teamDown(){
        if (accessToken != null){
            userRequest.delete().then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами неавторизированного пользователя")
    @Description("Тест проверяет, что, несмотря на положительный ответ сервера, " +
            "поля Владелец(owner) и Время создания(createdAt) не выходят, заказ не создается")
    public void createOrderUnauthorizedTest(){
        order.ingredientCreate();
        order.createOrderUnauthorized().then().assertThat()
                .statusCode(200)
                .body("order.createdAt", nullValue())
                .body("order.owner", nullValue());
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами авторизированного пользователя")
    @Description("Тест проверяет, что, поля Владелец(owner) и Время создания(createdAt) выходят, заказ создается")
    public void createOrderAuthorizedTest(){
        order.ingredientCreate();
        order.createOrderAuthorized().then().assertThat()
                .statusCode(200)
                .body("order.createdAt", notNullValue())
                .body("order.owner", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов авторизированного пользователя")
    @Description("Тест проверяет возврат ошибки в случае передачи запроса о создании заказа без ингредиентов " +
            "(у неавторизованного пользователя запрос отрабатывает так же)")
    public void createOrderWithoutIngredientsTest(){
        order.json = "{" + "\"ingredients\":[]" + "}";
        order.createOrderAuthorized().then().assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов авторизированного пользователя")
    @Description("Тест проверяет ошибки в случае указания неверного хеша ингредиента " +
            "(у неавторизованного пользователя запрос отрабатывает так же)")
    public void createOrderInvalidIngredientsTest(){
        IngredientList ingredientList = new IngredientList();
        ingredientList = ingredientList.getAllIngredients();
        String ingredientOrder = ingredientList.getData().get(1).get_id();
        order.json = "{" + "\"ingredients\":[\"" + ingredientOrder + "ghk2\"]}";
        order.createOrderAuthorized().then().assertThat().statusCode(500);
    }
}
