package com.example.calorie_counter_bmi.tests;

import com.example.calorie_counter_bmi.DBUtils;
import com.example.calorie_counter_bmi.models.Product;
import com.example.calorie_counter_bmi.models.User;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class RightBoardControllerTest {

    @Test
    public void addNewProduct () throws SQLException {
        String name = "Дыня";
        Double cal = 35.0;
        Double proteins = 0.6;
        Double fat = 0.3;
        Double carbs = 7.4;
        Double fiber = 0.9;
        boolean res = DBUtils.addNewProduct(name, cal, proteins, fat, carbs, fiber);
        assertTrue(res);
    }

    @Test
    public void changeWeight () throws SQLException {
        boolean res = DBUtils.updateUsersWeight(2, 45.9);
        assertTrue(res);
    }

    @Test
    public void addNewUser () throws SQLException {
        String name = "Sarah";
        String password = "sarah88";
        String gender = "female";
        int height = 176;
        Double weight = 85.0;
        boolean res = DBUtils.signUpUser(name, password, gender, height, weight);
        assertTrue(res);
    }

    @Test
    public void getUsersDailyIntakeProteins () {
        Double height = 175.0;
        Double weight = 70.0;
        String gender = "male";
        Double[] dailyIV = User.calculateDailyIntakeValues(height, weight, gender);
        Double prot = dailyIV[0];
        Assert.assertEquals(Double.valueOf(56.0), prot);
    }

    @Test
    public void getUsersDailyIntakeFat () {
        Double height = 175.0;
        Double weight = 70.0;
        String gender = "male";
        Double[] dailyIV = User.calculateDailyIntakeValues(height, weight, gender);
        Assert.assertEquals(Double.valueOf(62.01), dailyIV[1]);
    }

    @Test
    public void getUsersDailyIntakeCarbs () {
        Double height = 175.0;
        Double weight = 70.0;
        String gender = "male";
        Double[] dailyIV = User.calculateDailyIntakeValues(height, weight, gender);
        Assert.assertEquals(Double.valueOf(232.54), dailyIV[2]);
    }

    @Test
    public void getUsersDailyIntakeFiber () {
        Double height = 175.0;
        Double weight = 70.0;
        String gender = "male";
        Double[] dailyIV = User.calculateDailyIntakeValues(height, weight, gender);
        Assert.assertEquals(Double.valueOf(25.0), dailyIV[3]);
    }

    @Test
    public void getUsersDailyIntakeCal () {
        Double height = 175.0;
        Double weight = 70.0;
        String gender = "male";
        Double[] dailyIV = User.calculateDailyIntakeValues(height, weight, gender);
        Assert.assertEquals(Double.valueOf(1860.3), dailyIV[4]);
    }

    @Test
    public void calculateEatenProductIntakeCal () {
        Double amount = 150.0;
        Double caloriesPer100g = 340.0;
        Double proteinPer100g = 13.0;
        Double fatPer100g = 2.5;
        Double carbsPer100g = 61.0;
        Double fiberPer100g = 11.0;

        Double[] nutritionValues = Product.calculateNutrition(amount, caloriesPer100g, proteinPer100g, fatPer100g, carbsPer100g, fiberPer100g);
        Assert.assertEquals(Double.valueOf(510.0), nutritionValues[0]);
    }

    @Test
    public void calculateEatenProductIntakeProteins () {
        Double amount = 150.0;
        Double caloriesPer100g = 340.0;
        Double proteinPer100g = 13.0;
        Double fatPer100g = 2.5;
        Double carbsPer100g = 61.0;
        Double fiberPer100g = 11.0;

        Double[] nutritionValues = Product.calculateNutrition(amount, caloriesPer100g, proteinPer100g, fatPer100g, carbsPer100g, fiberPer100g);
        Assert.assertEquals(Double.valueOf(19.5), nutritionValues[1]);
    }

    @Test
    public void calculateEatenProductIntakeFat () {
        Double amount = 150.0;
        Double caloriesPer100g = 340.0;
        Double proteinPer100g = 13.0;
        Double fatPer100g = 2.5;
        Double carbsPer100g = 61.0;
        Double fiberPer100g = 11.0;

        Double[] nutritionValues = Product.calculateNutrition(amount, caloriesPer100g, proteinPer100g, fatPer100g, carbsPer100g, fiberPer100g);
        Assert.assertEquals(Double.valueOf(3.75), nutritionValues[2]);
    }

    @Test
    public void calculateEatenProductIntakeCarbs () {
        Double amount = 150.0;
        Double caloriesPer100g = 340.0;
        Double proteinPer100g = 13.0;
        Double fatPer100g = 2.5;
        Double carbsPer100g = 61.0;
        Double fiberPer100g = 11.0;

        Double[] nutritionValues = Product.calculateNutrition(amount, caloriesPer100g, proteinPer100g, fatPer100g, carbsPer100g, fiberPer100g);
        Assert.assertEquals(Double.valueOf(91.5), nutritionValues[3]);
    }

    @Test
    public void calculateEatenProductIntakeFiber () {
        Double amount = 150.0;
        Double caloriesPer100g = 340.0;
        Double proteinPer100g = 13.0;
        Double fatPer100g = 2.5;
        Double carbsPer100g = 61.0;
        Double fiberPer100g = 11.0;

        Double[] nutritionValues = Product.calculateNutrition(amount, caloriesPer100g, proteinPer100g, fatPer100g, carbsPer100g, fiberPer100g);
        Assert.assertEquals(Double.valueOf(16.5), nutritionValues[4]);
    }

    @Test
    public void getProductIdByName () {
        String name = "Банан";
        int res = DBUtils.getIdByProductName(name);
        Assert.assertEquals(4, res);
    }
}