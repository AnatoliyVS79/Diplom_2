package ru.yandex.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.stellarburgers.client.Order;
import ru.praktikum.stellarburgers.client.UserClient;
import ru.praktikum.stellarburgers.model.CreateOrderRequest;
import ru.praktikum.stellarburgers.model.RegisterUserRequest;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final Boolean authorize;
    private final Boolean success;
    private final Boolean checkSuccessResponse;
    private final Integer code;
    UserClient userClient;
    RegisterUserRequest registerUserRequest;
    ValidatableResponse validatableResponse;
    CreateOrderRequest createOrderRequest;
    private Order order;

    public CreateOrderTest(Boolean authorize, CreateOrderRequest createOrderRequest, Boolean success, Boolean checkSuccessResponse, Integer code) {
        this.authorize = authorize;
        this.createOrderRequest = createOrderRequest;
        this.success = success;
        this.checkSuccessResponse = checkSuccessResponse;
        this.code = code;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {true, new CreateOrderRequest(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa73"}), true, true, 200},
                {false, new CreateOrderRequest(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa73"}), true, true, 200},
                {true, new CreateOrderRequest(new String[]{}), true, false, 400},
                {false, new CreateOrderRequest(new String[]{}), true, false, 400},
                {true, new CreateOrderRequest(new String[]{"-61c0c5a71d1f82001bdaaa6d", "-61c0c5a71d1f82001bdaaa6f", "-61c0c5a71d1f82001bdaaa73"}), false, false, 500},
                {false, new CreateOrderRequest(new String[]{"-61c0c5a71d1f82001bdaaa6d", "-61c0c5a71d1f82001bdaaa6f", "-61c0c5a71d1f82001bdaaa73"}), false, false, 500},

        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        order = new Order();
        registerUserRequest = RegisterUserRequest.getRandom();
        validatableResponse = userClient.registerNewUserAndReturnResponse(registerUserRequest);
        userClient.saveUserToken(validatableResponse);
    }

    @After
    public void tearDown() {
        userClient.deleteUserAndFlushToken();
    }

    @Test
    @DisplayName("Проверка создания заказов")
    public void createOrderTest() {
        validatableResponse = order.createOrderAndReturnResponse(createOrderRequest, authorize)
                .assertThat()
                .statusCode(code);
        if (checkSuccessResponse) {
            validatableResponse.body("success", equalTo(success));
        }
    }
}
