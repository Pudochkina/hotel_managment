module com.example.hotel_managment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires jbcrypt;
    requires org.apache.pdfbox;

    opens com.example.hotel_managment to javafx.fxml;
    exports com.example.hotel_managment;
    exports com.example.hotel_managment.controllers;
    exports com.example.hotel_managment.models;
    exports com.example.hotel_managment.controllers.client;
    exports com.example.hotel_managment.views;
    exports com.example.hotel_managment.db;
    opens com.example.hotel_managment.db to javafx.fxml;
    exports com.example.hotel_managment.utils;
}