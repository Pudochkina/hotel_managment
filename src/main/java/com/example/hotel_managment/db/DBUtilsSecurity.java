package com.example.hotel_managment.db;

import com.example.hotel_managment.models.User;
import com.example.hotel_managment.views.ViewFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class DBUtilsSecurity {

    /** роль пользователя который зашел в систему
     * */
    public static String retrRole;

    /** метод отвечающий за регистрацию пользователя
     * */
    public static boolean signUpUser(String username, String password, User.Role role) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExist = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            psCheckUserExist = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExist.setString(1, username);
            resultSet = psCheckUserExist.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, role) " +
                        "VALUES (?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, hashPassword(password));
                psInsert.setString(3, role.toString());
                psInsert.executeUpdate();
                //System.out.println("User successfully created!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("User successfully created!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExist != null) {
                try {
                    psCheckUserExist.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (psInsert.isClosed()){
            return true;
        }else {
            return false;
        }
    }

    /** метод отвечающий за вход пользователя
     * считывает значения введенные пользователем
     * производит валидацию
     * берет из бд данные
     * и отображает на странице
     * */
    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            preparedStatement = connection.prepareStatement("SELECT password, role FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while (resultSet.next()){
                    retrRole = resultSet.getString("role");
                    String retrievedPassword = resultSet.getString("password");
                    if (BCrypt.checkpw(password, retrievedPassword)) {
                        ViewFactory.changeScene(event, "/fxml/client/main.fxml", "Отель Янтарь", username);
                    } else {
                        System.out.println("Password did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect!");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}

