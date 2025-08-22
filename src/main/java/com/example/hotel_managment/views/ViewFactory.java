package com.example.hotel_managment.views;

import com.example.hotel_managment.controllers.client.MainController;
import com.example.hotel_managment.db.DBUtilsGuest;
import com.example.hotel_managment.models.User;
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

    public void showMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/client/main.fxml"));
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
        stage.setTitle("Отель Янтарь");
        stage.show();
        stage.setResizable(false);
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username){
        Parent root = null;
        if (username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(DBUtilsGuest.class.getResource(fxmlFile));
                root = loader.load();
                MainController mainController = loader.getController();
                mainController.setSidebarUsername(username);
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
        stage.setX(100);
        stage.setY(70);
        stage.show();
    }


}
