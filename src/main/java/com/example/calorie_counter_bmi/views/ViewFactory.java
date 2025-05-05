package com.example.calorie_counter_bmi.views;

import com.example.calorie_counter_bmi.DBUtils;
import com.example.calorie_counter_bmi.controllers.client.RightBoardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    public ViewFactory(){}

    public void showRegisterWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
        createStage(loader);
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        createStage(loader);
    }

    public void showCenterWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/right_product_dashboard.fxml"));
        createStage(loader);
    }

    public void createStage(FXMLLoader loader){
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Calorie Counter");
        stage.show();
        stage.setResizable(false);
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public static void changeSceneFromLogInToCenterBoard (ActionEvent event, String fxmlFile, String title, String username, int id, Double weight, Double calories, Double proteins, Double carbo, Double fat, Double fiber){
        Parent root = null;
        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                RightBoardController rightBoardController = loader.getController();
                rightBoardController.setUserInformation(id, username, weight, calories, proteins, carbo, fat, fiber);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(ViewFactory.class.getResource(fxmlFile));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setX(20);
        stage.setY(20);
        stage.show();
    }
}
