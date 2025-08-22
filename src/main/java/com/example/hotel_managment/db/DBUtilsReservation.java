package com.example.hotel_managment.db;

import com.example.hotel_managment.models.*;
import com.example.hotel_managment.views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.*;

public class DBUtilsReservation {

    /**
     * считывание из бд данных о гостях
     */
    public static ObservableList<ReservationUS> getReservationsFromDB() {
        ObservableList<ReservationUS> reservationSearchObservableList = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement getReservation = null;
        ResultSet queryOutput = null;

        String sql = "SELECT r.reservation_id, g.guest_id, g.guest_fio, s.service_name, " +
                "rm.room_number, rm.type_of_room, r.date_in, r.date_out, r.reservation_status, " +
                "DATEDIFF(r.date_out, r.date_in) * (rmt.cost_per_night + IFNULL(s.service_cost, 0)) AS total_amount " +
                "FROM reservation r " +
                "LEFT JOIN guest g ON r.guest_id = g.guest_id " +
                "LEFT JOIN service s ON r.service_id = s.service_id " +
                "LEFT JOIN room rm ON r.room_id = rm.room_id " +
                "LEFT JOIN type_of_room rmt ON rm.type_of_room = rmt.type_of_room " +
                "WHERE r.reservation_status = 'Подтверждено' " +
                "ORDER BY r.date_in DESC";
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            getReservation = connection.prepareStatement(sql);
            queryOutput = getReservation.executeQuery();

            while (queryOutput.next()) {
                Integer queryReservationId = queryOutput.getInt("reservation_id");
                Integer queryGuestId = queryOutput.getInt("guest_id");
                String queryGuestFIO = queryOutput.getString("guest_fio");
                String queryServiceName = queryOutput.getString("service_name");
                String queryRoomNumber = queryOutput.getString("room_number");
                String queryRoomType = queryOutput.getString("type_of_room");
                Date queryDateIn = queryOutput.getDate("date_in");
                Date queryDateOut = queryOutput.getDate("date_out");
                Integer queryTotalAmount = queryOutput.getInt("total_amount");

                reservationSearchObservableList.add(new ReservationUS(queryReservationId, queryGuestId, queryGuestFIO,
                        queryServiceName, queryRoomNumber, queryRoomType, queryDateIn, queryDateOut, queryTotalAmount));
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
            if (getReservation != null) {
                try {
                    getReservation.close();
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
        return reservationSearchObservableList;
    }

    /**
     * метод отвечающий за добавление новой брони в бд
     */
    public static boolean addNewReservation(Integer guest_id, Integer service_id, Integer room_id, Date date_in, Date date_out) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psCheckGuestExist = null;
        ResultSet resultSet = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            psCheckGuestExist = connection.prepareStatement("SELECT * FROM reservation WHERE guest_id = ? " +
                                                            "AND service_id = ? AND room_id = ? AND date_in = ?" +
                                                            " AND date_out = ? AND reservation_status IN ('Подтверждено', 'Ожидает оплаты')");
            psCheckGuestExist.setInt(1, guest_id);
            psCheckGuestExist.setInt(2, service_id);
            psCheckGuestExist.setInt(3, room_id);
            psCheckGuestExist.setDate(4, date_in);
            psCheckGuestExist.setDate(5, date_out);
            resultSet = psCheckGuestExist.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("Reservation already exists!");
                return false;
            } else {
                psInsert = connection.prepareStatement("INSERT INTO " +
                        "reservation (guest_id, service_id, room_id, date_in, date_out, reservation_status) " +
                        "VALUES (?, ?, ?, ?, ?, 'Подтверждено')");
                psInsert.setInt(1, guest_id);
                psInsert.setInt(2, service_id);
                psInsert.setInt(3, room_id);
                psInsert.setDate(4, date_in);
                psInsert.setDate(5, date_out);
                psInsert.executeUpdate();
                System.out.println("Reservation successfully added!");

                psUpdate = connection.prepareStatement( "UPDATE room SET room_status = 'Занят' WHERE room_id = ?");
                psUpdate.setInt(1, room_id);
                psUpdate.executeUpdate();
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
     * метод отвечающий за изменение брони в бд
     */
    public static boolean updateReservation(Integer reservation_id, Integer guest_id, Integer service_id, Integer room_id, Date date_in, Date date_out) throws SQLException {
        Connection connection = null;
        PreparedStatement psCheckGuestExist = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            psCheckGuestExist = connection.prepareStatement("UPDATE reservation SET guest_id = ?, " +
                    "service_id = ?, room_id = ?, date_in = ?, " +
                    "date_out = ? WHERE reservation_id = ?");
            psCheckGuestExist.setInt(1, guest_id);
            psCheckGuestExist.setInt(2, service_id);
            psCheckGuestExist.setInt(3, room_id);
            psCheckGuestExist.setDate(4, date_in);
            psCheckGuestExist.setDate(5, date_out);
            psCheckGuestExist.setInt(6, reservation_id);
            int rowsAffected = psCheckGuestExist.executeUpdate();
            // If at least one row was updated, consider it successful
            success = rowsAffected > 0;

            if (success) {
                System.out.println("Reservation successfully updated!");
            } else {
                System.out.println("No reservation was updated (ID might not exist)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (psCheckGuestExist != null) {
                try {
                    psCheckGuestExist.close();
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
     * метод отвечающий за удаление брони из бд
     */
    public static boolean deleteReservation(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement deleteGuest = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            deleteGuest = connection.prepareStatement("DELETE FROM reservation WHERE reservation_id = ?");
            deleteGuest.setInt(1, id);
            deleteGuest.executeUpdate();
            System.out.println("Reservation deleted!");
        } catch (SQLIntegrityConstraintViolationException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Бронь нельзя удалить!");
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
     * метод отвечающий за оплату брони в бд
     */
    public static boolean SendReservationToPayments(Integer reservation_id, Integer sum, Date date, String method) throws SQLException {
        Connection connection = null;
        PreparedStatement psCheckGuestExist = null;
        PreparedStatement psInsert = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            psCheckGuestExist = connection.prepareStatement("UPDATE reservation r JOIN room rm " +
                    "ON r.room_id = rm.room_id SET r.reservation_status = 'Оплачено', "
                    + "rm.room_status = 'Свободен' WHERE r.reservation_id = ?");
            psCheckGuestExist.setInt(1, reservation_id);
            int rowsAffected = psCheckGuestExist.executeUpdate();
            // If at least one row was updated, consider it successful
            success = rowsAffected > 0;

            if (success) {
                System.out.println("Reservation successfully paid!");
            } else {
                System.out.println("No reservation was paid (ID might not exist)");
            }

            psInsert = connection.prepareStatement("INSERT INTO payment (reservation_id, payment_amount, payment_date, payment_method) VALUES (?, ?, ?, ?)");
            psInsert.setInt(1, reservation_id);
            psInsert.setInt(2, sum);
            psInsert.setDate(3, date);
            psInsert.setString(4, method);
            psInsert.executeUpdate();
            System.out.println("Payment successfully added!");
            success = true;

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (psCheckGuestExist != null) {
                try {
                    psCheckGuestExist.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }  if (psInsert != null) {
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
}

