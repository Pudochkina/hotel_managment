package com.example.calorie_counter_bmi.models;

import java.util.Map;
import java.util.stream.Collectors;

public class Product {

    final Integer id;
    final String productName;
    final Double product_calories_perg, product_protein_perg, product_fat_perg, product_carbs_perg, product_fiber_perg;

    public Product(Integer id, String product_name, Double product_calories_perg, Double product_protein_perg, Double product_fat_perg, Double product_carbs_perg, Double product_fiber_perg) {
        this.id = id;
        this.productName = product_name;
        this.product_calories_perg = product_calories_perg;
        this.product_protein_perg = product_protein_perg;
        this.product_fat_perg = product_fat_perg;
        this.product_carbs_perg = product_carbs_perg;
        this.product_fiber_perg = product_fiber_perg;
    }

    public Integer getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Double getProduct_calories_perg() {
        return product_calories_perg;
    }

    public Double getProduct_protein_perg() {
        return product_protein_perg;
    }

    public Double getProduct_fat_perg() {
        return product_fat_perg;
    }

    public Double getProduct_carbs_perg() {
        return product_carbs_perg;
    }

    public Double getProduct_fiber_perg() {
        return product_fiber_perg;
    }

    public void setId(Integer id) {
        id = id;
    }

    public void setProductName(String productName) {
        productName = productName;
    }

    public void setProduct_calories_perg(Double product_calories_perg) {
        product_calories_perg = product_calories_perg;
    }

    public void setProduct_protein_perg(Double product_protein_perg) {
        product_protein_perg = product_protein_perg;
    }

    public void setProduct_fat_perg(Double product_fat_perg) {
        product_fat_perg = product_fat_perg;
    }

    public void setProduct_carbs_perg(Double product_carbs_perg) {
        product_carbs_perg = product_carbs_perg;
    }

    public void setProduct_fiber_perg(Double product_fiber_perg) {
        product_fiber_perg = product_fiber_perg;
    }

    public static Double[] calculateNutrition(Double amount, Double caloriesPer100g,
                                              Double proteinPer100g, Double fatPer100g, Double carbsPer100g, Double fiberPer100g) {
        Double calories = Math.round((amount / 100) * caloriesPer100g * 100.0) / 100.0;
        Double protein =  Math.round((amount / 100) * proteinPer100g * 100.0) / 100.0;
        Double fat =  Math.round((amount / 100) * fatPer100g * 100.0) / 100.0;
        Double carbs =  Math.round((amount / 100) * carbsPer100g * 100.0) / 100.0;
        Double fiber =  Math.round((amount / 100) * fiberPer100g * 100.0) / 100.0;

        return new Double[]{calories, protein, fat, carbs, fiber};
    }

    public static void main(String[] args) {
        Double amount = 150.0; // вес или объем продукта в граммах или миллилитрах
        Double caloriesPer100g = 340.0; // калории на 100 г продукта
        Double proteinPer100g = 13.0; // белки на 100 г продукта
        Double fatPer100g = 2.5; // жиры на 100 г продукта
        Double carbsPer100g = 61.0; // углеводы на 100 г продукта
        Double fiberPer100g = 11.0; // клетчатка на 100 г продукта

        Double[] nutritionValues = calculateNutrition(amount, caloriesPer100g, proteinPer100g, fatPer100g, carbsPer100g, fiberPer100g);

        System.out.println("Употреблено калорий: " + nutritionValues[0]);
        System.out.println("Употреблено белков: " + nutritionValues[1] + " г");
        System.out.println("Употреблено жиров: " + nutritionValues[2] + " г");
        System.out.println("Употреблено углеводов: " + nutritionValues[3] + " г");
        System.out.println("Употреблено клетчатки: " + nutritionValues[4] + " г");


    }

}
