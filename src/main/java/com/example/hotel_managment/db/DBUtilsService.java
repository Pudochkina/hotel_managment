package com.example.hotel_managment.db;

import com.example.hotel_managment.models.*;
import com.example.hotel_managment.views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

