package com.example.calorie_counter_bmi;

import com.example.calorie_counter_bmi.controllers.client.RightBoardController;
import com.example.calorie_counter_bmi.models.EatenProduct;
import com.example.calorie_counter_bmi.models.Product;
import com.example.calorie_counter_bmi.models.User;
import com.example.calorie_counter_bmi.views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.sql.*;
import javafx.scene.control.Alert;

public class DBUtils {
    /** id пользователя который зашел в систему
     * */
    public static int retrid;

    /** метод отвечающий за регистрацию пользователя
     * считывает значения введенные пользователем
     * производит валидацию
     * рассчитывает кбжу
     * заносит в бд
     * */
    public static boolean signUpUser(String username, String password, String gender, int height, Double weight) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExist = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/calorie_counter_app", "root", "qwerty1234");
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
                while (resultSet.next()){
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

    /** считывание из бд даннфх о продуктах
     * */
    public static ObservableList<Product> getProductsFromDB(){
        ObservableList<Product> productSearchObservableList = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement getProduct = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/calorie_counter_app", "root", "qwerty1234");
            getProduct = connection.prepareStatement("SELECT product_id, product_name, product_cal_perg, product_protein_perg, product_fat_perg, product_carbs_perg, product_fiber_perg FROM products");
            queryOutput = getProduct.executeQuery();

            while (queryOutput.next()){
                Integer queryProductId = queryOutput.getInt("product_id");
                String queryProductName = queryOutput.getString("product_name");
                Double queryProductCalPerg = queryOutput.getDouble("product_cal_perg");
                Double queryProductProteinPerg = queryOutput.getDouble("product_protein_perg");
                Double queryProductFatPerg = queryOutput.getDouble("product_fat_perg");
                Double queryProductCarbsPerg = queryOutput.getDouble("product_carbs_perg");
                Double queryProductFiberPerg = queryOutput.getDouble("product_fiber_perg");

                productSearchObservableList.add(new Product(queryProductId, queryProductName, queryProductCalPerg, queryProductProteinPerg, queryProductFatPerg, queryProductCarbsPerg, queryProductFiberPerg));
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
            if (getProduct != null) {
                try {
                    getProduct.close();
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
        return productSearchObservableList;
    }

    /** метод отвечающий за добавление нового продукта в бд
     * */
    public static boolean addNewProduct(String name, Double calories, Double proteins, Double fat, Double carbs, Double fiber) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExist = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/calorie_counter_app", "root", "qwerty1234");
            psCheckUserExist = connection.prepareStatement("SELECT * FROM products WHERE product_name = ?");
            psCheckUserExist.setString(1, name);
            resultSet = psCheckUserExist.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("Product already exists!");
                //Alert alert = new Alert(Alert.AlertType.ERROR);
                //alert.setContentText("Product already exists!");
                //alert.show();
                return false;
            } else {
                psInsert = connection.prepareStatement("INSERT INTO " +
                        "products (product_name, product_cal_perg, product_protein_perg, product_fat_perg, product_carbs_perg, product_fiber_perg) " +
                        "VALUES (?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, name);
                psInsert.setDouble(2, calories);
                psInsert.setDouble(3, proteins);
                psInsert.setDouble(4, fat);
                psInsert.setDouble(5, carbs);
                psInsert.setDouble(6, fiber);
                psInsert.executeUpdate();
                System.out.println("Product successfully added!");
                //Alert alert = new Alert(Alert.AlertType.INFORMATION);
                //alert.setContentText("Product successfully added!");
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
        if(psInsert.isClosed()){
            return true;
        } else {
            return false;
        }
    }

    /** метод отвечающий за изменение веса пользователя
     * */
    public static boolean updateUsersWeight(Integer id, Double weight) throws SQLException {
        Connection connection = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psCheckUserExist = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/calorie_counter_app", "root", "qwerty1234");
            psCheckUserExist = connection.prepareStatement("SELECT user_name, user_gender, user_height FROM users WHERE user_id = ?");
            psCheckUserExist.setInt(1, id);
            resultSet = psCheckUserExist.executeQuery();

            while (resultSet.next()) {
                String queryName = resultSet.getString("user_name");
                String queryGender = resultSet.getString("user_gender");
                Integer queryHeight = resultSet.getInt("user_height");

                Double[] dailyIntakeValues = User.calculateDailyIntakeValues(Double.valueOf(queryHeight), weight, queryGender);
                psUpdate = connection.prepareStatement("UPDATE" +
                        " users SET user_weight = ?, user_dt_calories = ?, user_dt_proteins = ?, user_dt_fat = ?, user_dt_fiber = ?, user_dt_carbo = ?" +
                        "WHERE user_id = ?");
                psUpdate.setDouble(1, weight);
                psUpdate.setDouble(2, dailyIntakeValues[4]);
                psUpdate.setDouble(3, dailyIntakeValues[0]);
                psUpdate.setDouble(4, dailyIntakeValues[1]);
                psUpdate.setDouble(5, dailyIntakeValues[3]);
                psUpdate.setDouble(6, dailyIntakeValues[2]);
                psUpdate.setInt(7, id);
                psUpdate.executeUpdate();

                RightBoardController.setUpdatedUserInformation(id, queryName, weight, dailyIntakeValues[4], dailyIntakeValues[0], dailyIntakeValues[2], dailyIntakeValues[1], dailyIntakeValues[3]);
            }
            System.out.println("Your weight successfully updated!");
            //Alert alert = new Alert(Alert.AlertType.INFORMATION);
            //alert.setContentText("Your weight successfully updated!");
            //alert.show();

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
            if (psUpdate != null) {
                try {
                    psUpdate.close();
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
        if (psUpdate.isClosed()){
            return true;
        } else {
            return false;
        }
    }

    /** метод отвечающий за поиск id продукта по его названию
     * */
    public static Integer getIdByProductName(String productName){
        Integer p_id = null;

        Connection connection = null;
        PreparedStatement getProductId = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/calorie_counter_app", "root", "qwerty1234");
            getProductId = connection.prepareStatement("SELECT product_id FROM products WHERE product_name = ?");
            getProductId.setString(1, productName);
            queryOutput = getProductId.executeQuery();

            while (queryOutput.next()) {
                Integer queryProductId = queryOutput.getInt("product_id");
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
            if (getProductId != null) {
                try {
                    getProductId.close();
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

    /** считывание из бд данных о съеденных продуктах
     * */
    public static ObservableList<EatenProduct> getMenuFromDB(String date){
        ObservableList<EatenProduct> productSearchObservableList = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement getProduct = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/calorie_counter_app", "root", "qwerty1234");
            //getProduct = connection.prepareStatement("SELECT eaten_product_amount, eaten_product_calories, eaten_product_protein, eaten_product_fat, eaten_product_carbs, eaten_product_fiber, eaten_product_name FROM menu WHERE menu_date = " + date + " AND user_id = " + retrid);
            getProduct = connection.prepareStatement("SELECT menu_date, eaten_product_amount, eaten_product_calories, eaten_product_protein, eaten_product_fat, eaten_product_carbs, eaten_product_fiber, eaten_product_name FROM menu WHERE user_id = " + retrid);
            queryOutput = getProduct.executeQuery();

            while (queryOutput.next()){
                Date queryMenu_date = queryOutput.getDate("menu_date");
                Double queryProductAmount = queryOutput.getDouble("eaten_product_amount");
                Double queryProductCal = queryOutput.getDouble("eaten_product_calories");
                Double queryProductProtein = queryOutput.getDouble("eaten_product_protein");
                Double queryProductFat = queryOutput.getDouble("eaten_product_fat");
                Double queryProductCarbs = queryOutput.getDouble("eaten_product_carbs");
                Double queryProductFiber = queryOutput.getDouble("eaten_product_fiber");
                String queryProductName = queryOutput.getString("eaten_product_name");
                productSearchObservableList.add(new EatenProduct(queryProductName, queryMenu_date, queryProductAmount, queryProductCal, queryProductProtein, queryProductFat, queryProductCarbs, queryProductFiber));
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
            if (getProduct != null) {
                try {
                    getProduct.close();
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
        return productSearchObservableList;
    }
    /** метод отвечающий за добавление съеденного продукта в бд
     * */
    public static boolean addNewEatenProduct(String date, int product_id, Double amount, String name, Double calories, Double proteins, Double fat, Double carbs, Double fiber) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/calorie_counter_app", "root", "qwerty1234");
            psInsert = connection.prepareStatement("INSERT INTO " +
                        "menu (menu_date, eaten_product_amount, eaten_product_calories, eaten_product_protein, eaten_product_fat, " +
                    "eaten_product_carbs, eaten_product_fiber, product_id, user_id, eaten_product_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            psInsert.setString(1, date);
            psInsert.setDouble(2, amount);
            psInsert.setDouble(3, calories);
            psInsert.setDouble(4, proteins);
            psInsert.setDouble(5, fat);
            psInsert.setDouble(6, carbs);
            psInsert.setDouble(7, fiber);
            psInsert.setInt(8, product_id);
            psInsert.setInt(9, retrid);
            psInsert.setString(10, name);
            psInsert.executeUpdate();
            System.out.println("Eaten Product successfully added!");
            //Alert alert = new Alert(Alert.AlertType.INFORMATION);
            //alert.setContentText("Eaten Product successfully added!");
            //alert.show();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
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
        } else {
            return false;
        }
    }
}
