package com.example.calorie_counter_bmi.models;

public class User {


    public static Double[] calculateDailyIntakeValues(Double height, Double weight, String gender) {
        Double[] intakeValues = new Double[5]; // Создаем массив для хранения значений

        Double bmr;
        // Рассчитываем базовый уровень метаболизма в зависимости от пола
        if (gender.equalsIgnoreCase("male")) {
            bmr =  Math.round((88.362 + (13.397 * weight) + (4.799 * height) - (5.677)) * 100.0) / 100.0;
        } else if (gender.equalsIgnoreCase("female")) {
            bmr =  Math.round((447.593 + (9.247 * weight) + (3.098 * height) - (4.330)) * 100.0) / 100.0;
        } else {
            throw new IllegalArgumentException("Некорректно указан пол. Введите 'male' или 'female'.");
        }

        // Рассчет суточной нормы потребления белков, жиров, углеводов, клетчатки
        Double proteinIntake = Math.round((0.8 * weight) * 100.0) / 100.0; // Примерное количество белков в граммах
        Double fatIntake = Math.round((0.3 * bmr / 9) * 100.0) / 100.0; // Примерное количество жиров в граммах
        Double carbIntake = Math.round((0.5 * bmr / 4) * 100.0) / 100.0; // Примерное количество углеводов в граммах
        Double fiberIntake = 25.0; // Примерное количество клетчатки в граммах

        intakeValues[0] = proteinIntake;
        intakeValues[1] = fatIntake;
        intakeValues[2] = carbIntake;
        intakeValues[3] = fiberIntake;
        intakeValues[4] = bmr;

        return intakeValues;
    }

    public static void main(String[] args) {
        Double height = 175.0; // Рост в см
        Double weight = 70.0; // Вес в кг
        String gender = "male"; // Пол: male или female

        Double[] dailyIntakeValues = calculateDailyIntakeValues(height, weight, gender);

        System.out.println("Дневная норма потребления белков: " + dailyIntakeValues[0] + " г");
        System.out.println("Дневная норма потребления жиров: " + dailyIntakeValues[1] + " г");
        System.out.println("Дневная норма потребления углеводов: " + dailyIntakeValues[2] + " г");
        System.out.println("Дневная норма потребления клетчатки: " + dailyIntakeValues[3] + " г");
        System.out.println("Дневная норма потребления калорий: " + dailyIntakeValues[4]);
    }


}
