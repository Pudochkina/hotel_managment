package com.example.hotel_managment.db;

import com.example.hotel_managment.models.*;
import com.example.hotel_managment.views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.sql.*;

import javafx.scene.control.Alert;

public class DBUtilsGuest {

    /**
     * считывание из бд данных о гостях
     */
    public static ObservableList<Guest> getGuestsFromDB()  {
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
    public static boolean updateGuest(Integer id, String fio, String phone, String email, Date date_birth, String passport) throws SQLException {
        Connection connection = null;
        PreparedStatement updateGuest = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            updateGuest = connection.prepareStatement("UPDATE guest SET guest_fio = ?, guest_phone = ?, guest_email = ?, " +
                    "guest_date_birth = ?, guest_passport = ? WHERE guest_id = ?");
            updateGuest.setString(1, fio);
            updateGuest.setString(2, phone);
            updateGuest.setString(3, email);
            updateGuest.setDate(4, date_birth);
            updateGuest.setString(5, passport);
            updateGuest.setInt(6, id);
            int rowsAffected = updateGuest.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected);
            System.out.println("Guest updated added!");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating guest: " + e.getMessage());
            e.printStackTrace();
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

