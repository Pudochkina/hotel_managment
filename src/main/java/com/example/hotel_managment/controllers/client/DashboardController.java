package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.views.ViewFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Label username_lb;
    public Button guests_menu_btn, rooms_menu_btn, services_menu_btn, reservation_menu_btn, payments_menu_btn, type_of_rooms_menu_btn1, logout_btn;
    public Button reservation_update_menu_btn1;
    public VBox sidebarPane, contentPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
        /**
         * метод который вызывается в классе DBUtils в методе changeScene
         * используется для того, чтобы при входе в аккаунт
         * данные пользователя отобразились на странице
         */
    public void setUserInformation(String name){
        username_lb.setText(name);
    }

    public void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            VBox content = loader.load();
            contentPane.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultContent() {
        loadContent("/fxml/client/dashboard_type_of_rooms.fxml");
    }
}
