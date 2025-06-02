package com.example.hotel_managment.controllers;

import com.example.hotel_managment.db.DBUtilsGuest;
import com.example.hotel_managment.db.DBUtilsSecurity;
import com.example.hotel_managment.models.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public Label error_text_fld;
    public TextField username_txt_fld, password_txt_fld;
    public Button login_btn, clear_btn, register_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //login_btn.setOnAction(event -> onLogin());
        register_btn.setOnAction(event -> onRegister());

        clear_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                username_txt_fld.setText("");
                password_txt_fld.setText("");
            }
        });

        login_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtilsSecurity.logInUser(actionEvent, username_txt_fld.getText(), password_txt_fld.getText());
            }
        });
    }

    private void onRegister() {
        Stage stage = (Stage) register_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showRegisterWindow();
    }
}
