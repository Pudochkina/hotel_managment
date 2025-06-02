package com.example.hotel_managment;

import com.example.hotel_managment.models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        //Model.getInstance().getViewFactory().showMainWindow();
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}