package com.example.calorie_counter_bmi.models;

import java.sql.Date;

public class EatenProduct {
    final String product_name;
    final Date menu_date;
    final Double eaten_product_amount;
    final Double eaten_product_calories;
    Double eaten_product_protein;
    Double eaten_product_fat;
    Double eaten_product_carbs;
    Double eaten_product_fiber;

    public EatenProduct(String product_name, Date menu_date1, Double eaten_product_amount, Double eaten_product_calories, Double eaten_product_protein, Double eaten_product_fat, Double eaten_product_carbs, Double eaten_product_fiber) {
        this.product_name = product_name;
        this.menu_date = menu_date1;
        this.eaten_product_amount = eaten_product_amount;
        this.eaten_product_calories = eaten_product_calories;
        this.eaten_product_protein = eaten_product_protein;
        this.eaten_product_fat = eaten_product_fat;
        this.eaten_product_carbs = eaten_product_carbs;
        this.eaten_product_fiber = eaten_product_fiber;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        product_name = product_name;
    }

    public Double getEaten_product_amount() {
        return eaten_product_amount;
    }

    public void setEaten_product_amount(Double eaten_product_amount) {
        eaten_product_amount = eaten_product_amount;
    }

    public Double getEaten_product_calories() {
        return eaten_product_calories;
    }

    public void setEaten_product_calories(Double eaten_product_calories) {
        eaten_product_calories = eaten_product_calories;
    }

    public Double getEaten_product_protein() {
        return eaten_product_protein;
    }

    public Date getMenu_date() {
        return menu_date;
    }

    public void setEaten_product_protein(Double eaten_product_protein) {
        this.eaten_product_protein = eaten_product_protein;
    }

    public Double getEaten_product_fat() {
        return eaten_product_fat;
    }

    public void setEaten_product_fat(Double eaten_product_fat) {
        this.eaten_product_fat = eaten_product_fat;
    }

    public Double getEaten_product_carbs() {
        return eaten_product_carbs;
    }

    public void setEaten_product_carbs(Double eaten_product_carbs) {
        this.eaten_product_carbs = eaten_product_carbs;
    }

    public Double getEaten_product_fiber() {
        return eaten_product_fiber;
    }

    public void setEaten_product_fiber(Double eaten_product_fiber) {
        this.eaten_product_fiber = eaten_product_fiber;
    }
}
