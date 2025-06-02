package com.example.hotel_managment.controllers.client;

import com.example.hotel_managment.views.ViewFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public Label username_lbl;
    public Button guests_menu_btn;
    public Button rooms_menu_btn;
    public Button services_menu_btn;
    public Button reservation_menu_btn;
    public Button payments_menu_btn;
    public Button type_of_rooms_menu_btn1;
    public Button reservation_update_menu_btn1;
    public Button logout_btn;
    public AnchorPane sidebarPane, contentPane;
    private Button currentlySelectedButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDefaultContent();
        //setupButtonActions();

        /**
         * обработка кнопки выход из аккаунта
         */
        logout_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ViewFactory.changeScene(actionEvent, "/fxml/login.fxml", "Log In", null);
            }
        });
    }

    private void setupButtonActions() {
        guests_menu_btn.setOnAction(e -> loadContent("/fxml/client/guests.fxml"));
        rooms_menu_btn.setOnAction(e -> loadContent("/fxml/client/rooms.fxml"));
        services_menu_btn.setOnAction(e -> loadContent("/fxml/client/services.fxml"));
        reservation_menu_btn.setOnAction(e -> loadContent("/fxml/client/reservation.fxml"));
        type_of_rooms_menu_btn1.setOnAction(e -> loadContent("/fxml/client/type_of_rooms.fxml"));
        reservation_update_menu_btn1.setOnAction(e -> loadContent("/fxml/client/payment.fxml"));
    }

    public void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane content = loader.load();
            contentPane.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDefaultContent() {
        loadContent("/fxml/client/guests.fxml");
    }

    public void setSidebarUsername(String username) {
        username_lbl.setText(username);
    }

    @FXML
    public void handleSidebarButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        // Снимаем выделение с предыдущей кнопки
        if (currentlySelectedButton != null) {
            currentlySelectedButton.getStyleClass().remove("active");
        }

        // Добавляем выделение новой кнопке
        clickedButton.getStyleClass().add("active");
        currentlySelectedButton = clickedButton;

        // Загрузка соответствующего контента
        switch (clickedButton.getId()) {
            case "guests_menu_btn":
                loadContent("/fxml/client/guests.fxml");
                break;
            case "rooms_menu_btn":
                loadContent("/fxml/client/rooms.fxml");
                break;
            case "services_menu_btn":
                loadContent("/fxml/client/services.fxml");
                break;
            case "reservation_menu_btn":
                loadContent("/fxml/client/reservation.fxml");
                break;
            case "type_of_rooms_menu_btn1":
                loadContent("/fxml/client/type_of_rooms.fxml");
                break;
            case "reservation_update_menu_btn1":
                loadContent("/fxml/client/payment.fxml");
                break;
        }
    }
}
