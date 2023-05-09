package ru.praktikum.stellarburgers.model;

public class CreateOrderRequest extends Request {
    private final String[] ingredients;

    public CreateOrderRequest(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getIngredients() {
        return ingredients;
    }
}
