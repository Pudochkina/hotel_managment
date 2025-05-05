package com.example.calorie_counter_bmi.controllers;

import com.example.calorie_counter_bmi.DBUtils;
import com.example.calorie_counter_bmi.models.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public RadioButton male_radio_btn;
    public RadioButton female_radio_btn;
    public Spinner<Integer> height_spinner;
    public TextField username_txt_fld;
    public TextField password_txt_fld;
    public Button register_btn;
    public Button clear_btn;
    public Button login_btn;
    public TextField users_weight_txt_fld;
    int currentValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        male_radio_btn.setToggleGroup(toggleGroup);
        female_radio_btn.setToggleGroup(toggleGroup);
        female_radio_btn.setSelected(true);

        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(120,220);
        spinnerValueFactory.setValue(120);
        height_spinner.setValueFactory(spinnerValueFactory);

        login_btn.setOnAction(event -> onLogin());

        register_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String toggleName = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
                currentValue = height_spinner.getValue();

                if (!username_txt_fld.getText().trim().isEmpty() && !password_txt_fld.getText().trim().isEmpty() && !users_weight_txt_fld.getText().trim().isEmpty()){
                    try {
                        boolean res =DBUtils.signUpUser(username_txt_fld.getText(), password_txt_fld.getText(), toggleName, currentValue, Double.valueOf(users_weight_txt_fld.getText()));
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
                }
                else if (!checkUsingIsDigitMethod(users_weight_txt_fld.getText())){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Only numbers allowed!");
                    alert.show();
                }
                else if (!users_weight_txt_fld.getText().contains(".")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Use a dot!");
                    alert.show();
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
                users_weight_txt_fld.setText("");
                password_txt_fld.setText("");
            }
        });
    }

    private void onLogin() {
        Stage stage = (Stage) login_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    public static boolean checkUsingIsDigitMethod(String input) {
        int counter = 0;
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                counter++;
            }
        }
        if (counter == 0){
            return true;
        }
        return false;
    }
}
