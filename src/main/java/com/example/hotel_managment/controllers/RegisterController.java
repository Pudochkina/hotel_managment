package com.example.hotel_managment.controllers;

import com.example.hotel_managment.db.DBUtilsGuest;
import com.example.hotel_managment.db.DBUtilsSecurity;
import com.example.hotel_managment.models.Model;
import com.example.hotel_managment.models.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    public TextField username_txt_fld, password_txt_fld;
    public Button register_btn, clear_btn, login_btn;
    public ComboBox<User.Role> role_combo_box;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        role_combo_box.getItems().setAll(User.Role.values());
        role_combo_box.setValue(User.Role.MANAGER);

        login_btn.setOnAction(event -> onLogin());

        register_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                User.Role role = role_combo_box.getValue();
                if (!username_txt_fld.getText().trim().isEmpty() && !password_txt_fld.getText().trim().isEmpty()){
                    try {
                        boolean res = DBUtilsSecurity.signUpUser(username_txt_fld.getText(), password_txt_fld.getText(), role);
                        if (res){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("User successfully created!");
                            alert.show();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("An error has occurred!");
                            alert.show();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Please fill in all information!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });

        clear_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                username_txt_fld.setText("");
                password_txt_fld.setText("");
            }
        });
    }

    private void onLogin() {
        Stage stage = (Stage) login_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

}
