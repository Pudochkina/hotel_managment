module com.example.calorie_counter_bmi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires junit;

    opens com.example.calorie_counter_bmi to javafx.fxml;
    exports com.example.calorie_counter_bmi;
    exports com.example.calorie_counter_bmi.controllers;
    exports com.example.calorie_counter_bmi.models;
    exports com.example.calorie_counter_bmi.controllers.client;
    exports com.example.calorie_counter_bmi.views;
    exports com.example.calorie_counter_bmi.tests;
}