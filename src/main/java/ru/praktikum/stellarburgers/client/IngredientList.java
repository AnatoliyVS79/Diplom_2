package ru.praktikum.stellarburgers.client;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomUtils;
import ru.praktikum.stellarburgers.model.BaseData;

import java.util.List;

import static io.restassured.RestAssured.given;

public class IngredientList extends BaseData {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Step("Вывод списка всех ингредиентов в виде объекта {IngredientList}")
    public IngredientList getAllIngredients(){
        IngredientList ingredientList = given()
                .spec(getBaseSpec())
                .get(BASE_URL + "ingredients/")
                .body()
                .as(IngredientList.class);
        return ingredientList;
    }

    String pp = ",";
    String orderRequest = "\"ingredients\":[";
    public void ingredientRequestCreate(){
        IngredientList ingredientList = new IngredientList();
        ingredientList = ingredientList.getAllIngredients();
        int randomCountIngredient = RandomUtils.nextInt(1, 4);
        int ingredientCount = ingredientList.getData().size();

        for (int i = 1; i<= randomCountIngredient; i++){
            int randomIngredient = RandomUtils.nextInt(0, ingredientCount);
            if (i < randomCountIngredient){
                pp = ",";
            }else {pp = "";}
            String ingredientOrder = ingredientList.getData().get(randomIngredient).get_id();
            orderRequest = orderRequest + "\"" + ingredientOrder + "\"" + pp;
        }
        orderRequest =orderRequest + "]";
    }
}
