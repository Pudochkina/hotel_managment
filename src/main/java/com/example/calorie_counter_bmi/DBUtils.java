package com.example.calorie_counter_bmi;

import com.example.calorie_counter_bmi.models.Guest;
import com.example.calorie_counter_bmi.models.User;
import com.example.calorie_counter_bmi.views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.sql.*;
import javafx.scene.control.Alert;

public class DBUtils {
    /**
     * id пользователя который зашел в систему
     */
    public static int retrid;

    /**
     * метод отвечающий за регистрацию пользователя
     * считывает значения введенные пользователем
     * производит валидацию
     * рассчитывает кбжу
     * заносит в бд
     */
    public static boolean signUpUser(String username, String password, String gender, int height, Double weight) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExist = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            psCheckUserExist = connection.prepareStatement("SELECT * FROM users WHERE user_name = ?");
            psCheckUserExist.setString(1, username);
            resultSet = psCheckUserExist.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username.");
                alert.show();
            } else {
                Double[] dailyIntakeValues = User.calculateDailyIntakeValues(Double.valueOf(height), weight, gender);
                psInsert = connection.prepareStatement("INSERT INTO " +
                        "users (user_name, user_password, user_gender, user_height," +
                        " user_weight, user_dt_calories, user_dt_proteins, user_dt_fat, user_dt_fiber, user_dt_carbo) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, gender);
                psInsert.setInt(4, height);
                psInsert.setDouble(5, weight);
                psInsert.setDouble(6, dailyIntakeValues[4]);
                psInsert.setDouble(7, dailyIntakeValues[0]);
                psInsert.setDouble(8, dailyIntakeValues[1]);
                psInsert.setDouble(9, dailyIntakeValues[3]);
                psInsert.setDouble(10, dailyIntakeValues[2]);
                psInsert.executeUpdate();

                System.out.println("User successfully created!");
                //Alert alert = new Alert(Alert.AlertType.INFORMATION);
                //alert.setContentText("User successfully created!");
                //alert.show();
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
        if (psInsert.isClosed()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * метод отвечающий за вход пользователя
     * считывает значения введенные пользователем
     * производит валидацию
     * берет из бд данные
     * и отображает на странице
     */
    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/calorie_counter_app", "root", "qwerty1234");
            preparedStatement = connection.prepareStatement("SELECT user_id, user_password, user_weight, user_dt_calories, user_dt_proteins, user_dt_fat, user_dt_fiber, user_dt_carbo FROM users WHERE user_name = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while (resultSet.next()) {
                    retrid = resultSet.getInt("user_id");
                    String retrievedPassword = resultSet.getString("user_password");
                    Double retrievedWeight = resultSet.getDouble("user_weight");
                    Double retr_dt_user_calories = resultSet.getDouble("user_dt_calories");
                    Double retr_user_dt_proteins = resultSet.getDouble("user_dt_proteins");
                    Double retr_user_dt_fat = resultSet.getDouble("user_dt_fat");
                    Double retr_user_dt_fiber = resultSet.getDouble("user_dt_fiber");
                    Double retr_user_dt_carbo = resultSet.getDouble("user_dt_carbo");
                    if (retrievedPassword.equals(password)) {
                        ViewFactory.changeSceneFromLogInToCenterBoard(event, "/fxml/client/right_product_dashboard.fxml", "Calorie Counter", username, retrid, retrievedWeight, retr_dt_user_calories, retr_user_dt_proteins, retr_user_dt_carbo, retr_user_dt_fat, retr_user_dt_fiber);
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
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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
    }

    /**
     * считывание из бд даннфх о гостях
     */
    public static ObservableList<Guest> getGuestsFromDB() {
        ObservableList<Guest> guestSearchObservableList = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement getGuest = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            getGuest = connection.prepareStatement("SELECT guest_id, guest_fio, guest_phone, guest_email, guest_date_birth, guest_passport FROM guest ORDER BY guest_fio ASC");
            queryOutput = getGuest.executeQuery();

            while (queryOutput.next()) {
                Integer queryGuestId = queryOutput.getInt("guest_id");
                String queryGuestFIO = queryOutput.getString("guest_fio");
                String queryGuestPhone = queryOutput.getString("guest_phone");
                String queryGuestEmail = queryOutput.getString("guest_email");
                Date queryGuestDB = queryOutput.getDate("guest_date_birth");
                String queryGuestPassport = queryOutput.getString("guest_passport");


                guestSearchObservableList.add(new Guest(queryGuestId, queryGuestFIO, queryGuestPhone, queryGuestEmail, queryGuestDB, queryGuestPassport));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (queryOutput != null) {
                try {
                    queryOutput.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (getGuest != null) {
                try {
                    getGuest.close();
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
        return guestSearchObservableList;
    }

    /**
     * метод отвечающий за добавление нового гостя в бд
     */
    public static boolean addNewGuest(String fio, String phone, String email, Date date_birth, String passport) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckGuestExist = null;
        ResultSet resultSet = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            psCheckGuestExist = connection.prepareStatement("SELECT * FROM guest WHERE guest_fio = ? AND guest_phone = ?");
            psCheckGuestExist.setString(1, fio);
            psCheckGuestExist.setString(2, phone);
            resultSet = psCheckGuestExist.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("Guest already exists!");
                return false;
            } else {
                psInsert = connection.prepareStatement("INSERT INTO " +
                        "guest (guest_fio, guest_phone, guest_email, guest_date_birth, guest_passport) " +
                        "VALUES (?, ?, ?, ?, ?)");
                psInsert.setString(1, fio);
                psInsert.setString(2, phone);
                psInsert.setString(3, email);
                psInsert.setDate(4, date_birth);
                psInsert.setString(5, passport);
                psInsert.executeUpdate();
                System.out.println("Guest successfully added!");
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckGuestExist != null) {
                try {
                    psCheckGuestExist.close();
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
        return success;
    }

    /**
     * метод отвечающий за изменение данных о госте в бд
     */
    public static boolean updateGuest(String currFio, String currPhone, String fio, String phone, String email, Date date_birth, String passport) throws SQLException {
        Connection connection = null;
        PreparedStatement updateGuest = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            updateGuest = connection.prepareStatement("UPDATE guest SET guest_fio = ?, guest_phone = ?, guest_email = ?, " +
                    "guest_date_birth = ?, guest_passport = ? WHERE guest_fio = ? AND guest_phone = ?");
            updateGuest.setString(1, fio);
            updateGuest.setString(2, phone);
            updateGuest.setString(3, email);
            updateGuest.setDate(4, date_birth);
            updateGuest.setString(5, passport);
            updateGuest.setString(6, currFio);
            updateGuest.setString(7, currPhone);
            updateGuest.executeUpdate();

            System.out.println("Guest updated added!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (updateGuest != null) {
                try {
                    updateGuest.close();
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
        if (updateGuest.isClosed()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * метод отвечающий за удаление гостя из бд
     */
    public static boolean deleteGuest(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement deleteGuest = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            deleteGuest = connection.prepareStatement("DELETE FROM guest WHERE guest_id = ?");
            deleteGuest.setInt(1, id);
            deleteGuest.executeUpdate();

            System.out.println("Guest deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (deleteGuest != null) {
                try {
                    deleteGuest.close();
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
        if (deleteGuest.isClosed()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * метод отвечающий за поиск id гостя по его телефону
     */
    public static Integer getIdByGuestPhone(String guest_phone) {
        Integer p_id = null;

        Connection connection = null;
        PreparedStatement getGuestId = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            getGuestId = connection.prepareStatement("SELECT guest_id FROM guest WHERE guest_phone = ?");
            getGuestId.setString(1, guest_phone);
            queryOutput = getGuestId.executeQuery();

            while (queryOutput.next()) {
                Integer queryProductId = queryOutput.getInt("guest_id");
                p_id = queryProductId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (queryOutput != null) {
                try {
                    queryOutput.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (getGuestId != null) {
                try {
                    getGuestId.close();
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
        return p_id;
    }

}

