package ru.praktikum.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.runner.Request;
import ru.praktikum.stellarburgers.model.BaseData;
import ru.praktikum.stellarburgers.model.Token;

import static io.restassured.RestAssured.given;

public class Order extends BaseData {
    public static final String ORDER_PATH = BASE_URL + "orders/";
    String json;
    IngredientList ingredientList = new IngredientList();

    public String ingredientCreate(){
        ingredientList.ingredientRequestCreate();
        json = "{" + ingredientList.orderRequest + "}";
        return json;
    }

    @Step("Создание заказа неавторизованного пользователя")
    public Response createOrderUnauthorized(){
        return given()
                .spec(getBaseSpec())
                .body(json)
                .port(ORDER_PATH);
    }

    @Step("Создание заказа авторизированного пользователя")
    public Response createOrderAuthorized(){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(Token.getAccessToken())
                .body(json)
                .port(ORDER_PATH);
    }

    @Step("Вывод заказов авторизаванного пользователя")
    public Response getUserOrderAuthorized(){
        return given()
                .spec(getBaseSpec())
                .get(ORDER_PATH);
    }

    @Step("Вывод заказов неавторизованного пользователя")
    public Response getUserOrderUnauthorized(){
        return given()
                .spec(getBaseSpec())
                .get(ORDER_PATH);
    }

}
