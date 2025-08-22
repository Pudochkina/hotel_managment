package com.example.hotel_managment.db;

import com.example.hotel_managment.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class DBUtilsService {

    /**
     * метод отвечающий за поиск id комнаты по ее номеру
     */
    public static Integer getServiceIdByServiceName (String service_name) {
        Integer p_id = null;

        Connection connection = null;
        PreparedStatement getServiceId = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            getServiceId = connection.prepareStatement("SELECT service_id FROM service WHERE service_name = ?");
            getServiceId.setString(1, service_name);
            queryOutput = getServiceId.executeQuery();

            while (queryOutput.next()) {
                Integer queryProductId = queryOutput.getInt("service_id");
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
            if (getServiceId != null) {
                try {
                    getServiceId.close();
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
    /**
     * метод отвечающий за добавление нового гостя в бд
     */
    public static boolean addNewService(String name, Double cost, String type) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckServiceExist = null;
        ResultSet resultSet = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            psCheckServiceExist = connection.prepareStatement("SELECT * FROM service WHERE service_name = ?");
            psCheckServiceExist.setString(1, name);
            resultSet = psCheckServiceExist.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("Service already exists!");
                return false;
            } else {
                psInsert = connection.prepareStatement("INSERT INTO " +
                        "service (service_name, service_cost, service_type) VALUES (?, ?, ?)");
                psInsert.setString(1, name);
                psInsert.setDouble(2, cost);
                psInsert.setString(3, type);
                psInsert.executeUpdate();
                System.out.println("Service successfully added!");
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
            if (psCheckServiceExist != null) {
                try {
                    psCheckServiceExist.close();
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
    public static boolean updateService(Integer id, String name, Double cost, String type) throws SQLException {
        Connection connection = null;
        PreparedStatement updateService = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            updateService = connection.prepareStatement("UPDATE service SET service_name = ?, service_cost = ?, service_type = ? " +
                    "WHERE service_id = ?");
            updateService.setString(1, name);
            updateService.setDouble(2, cost);
            updateService.setString(3, type);
            updateService.setInt(4, id);
            int rowsAffected = updateService.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected);
            System.out.println("Service updated added!");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating service: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * метод отвечающий за удаление услуги из бд
     */
    public static boolean deleteService(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement deleteService = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            deleteService = connection.prepareStatement("DELETE FROM service WHERE service_id = ?");
            deleteService.setInt(1, id);
            deleteService.executeUpdate();

            System.out.println("Service deleted!");
        } catch (SQLIntegrityConstraintViolationException ex) {
             Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Услугу нельзя удалить!");
            alert.show();
            throw ex;
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
            if (deleteService != null) {
                try {
                    deleteService.close();
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
        if (deleteService.isClosed()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * считывание из бд данных об услугах
     */
    public static ObservableList<Service> getServiceFromDB() {
        ObservableList<Service> ServiceSearchObservableList = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement getService = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            getService = connection.prepareStatement("SELECT service_id, service_name, service_cost, service_type FROM service");
            queryOutput = getService.executeQuery();

            while (queryOutput.next()) {
                Integer queryServiceId = queryOutput.getInt("service_id");
                String queryServiceName = queryOutput.getString("service_name");
                Integer queryServiceCost = queryOutput.getInt("service_cost");
                String queryServiceType = queryOutput.getString("service_type");

                ServiceSearchObservableList.add(new Service(queryServiceId,queryServiceCost, queryServiceName, queryServiceType));
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
            if (getService != null) {
                try {
                    getService.close();
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
        return ServiceSearchObservableList;
    }

    public static Double getServicePriceByName(String serviceName) {
        Connection connection = null;
        PreparedStatement getServiceId = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            getServiceId = connection.prepareStatement("SELECT service_cost FROM service WHERE service_name = ?");
            getServiceId.setString(1, serviceName);
            queryOutput = getServiceId.executeQuery();

            while (queryOutput.next()) {
                return queryOutput.getDouble("service_cost");
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
            if (getServiceId != null) {
                try {
                    getServiceId.close();
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
        return 0.0;
    }
}

