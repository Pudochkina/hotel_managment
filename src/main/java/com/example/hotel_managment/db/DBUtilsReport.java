package com.example.hotel_managment.db;

import com.example.hotel_managment.models.RoomIncome;
import com.example.hotel_managment.models.ServiceIncome;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBUtilsReport {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_managment_system";
    private static final String USER = "root";
    private static final String PASS = "qwerty1234";

    // Метод для получения дохода по типам номеров за период
    public static List<RoomIncome> getRoomIncomeByPeriod(LocalDate startDate, LocalDate endDate) {
        List<RoomIncome> result = new ArrayList<>();

        String sql = "SELECT tor.type_of_room, COUNT(r.reservation_id) as bookings_count, " +
                "SUM(p.payment_amount) as income " +
                "FROM payment p " +
                "JOIN reservation r ON p.reservation_id = r.reservation_id " +
                "JOIN room rm ON r.room_id = rm.room_id " +
                "JOIN type_of_room tor ON rm.type_of_room = tor.type_of_room " +
                "WHERE p.payment_date BETWEEN ? AND ? " +
                "GROUP BY tor.type_of_room " +
                "ORDER BY income DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new RoomIncome(
                            rs.getString("type_of_room"),
                            rs.getInt("bookings_count"),
                            rs.getDouble("income")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Данные успешно получены!");
        return result;
    }

    // Метод для получения дохода по типам услуг за период
    public static List<ServiceIncome> getServiceIncomeByPeriod(LocalDate startDate, LocalDate endDate) {
        List<ServiceIncome> result = new ArrayList<>();

        String sql = "SELECT s.service_type, COUNT(r.reservation_id) as bookings_count, " +
                "SUM(p.payment_amount) as income " +
                "FROM payment p " +
                "JOIN reservation r ON p.reservation_id = r.reservation_id " +
                "JOIN service s ON r.service_id = s.service_id " +
                "WHERE p.payment_date BETWEEN ? AND ? " +
                "GROUP BY s.service_type " +
                "ORDER BY income DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new ServiceIncome(
                            rs.getString("service_type"),
                            rs.getInt("bookings_count"),
                            rs.getDouble("income")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Данные успешно получены!");
        return result;
    }

    // Метод для получения дохода по типам номеров
    public static List<RoomIncome> getRoomIncome() {
        List<RoomIncome> result = new ArrayList<>();
        String sql = "SELECT tor.type_of_room, COUNT(r.reservation_id) as bookings_count, " +
                "SUM(p.payment_amount) as income " +
                "FROM payment p " +
                "JOIN reservation r ON p.reservation_id = r.reservation_id " +
                "JOIN room rm ON r.room_id = rm.room_id " +
                "JOIN type_of_room tor ON rm.type_of_room = tor.type_of_room " +
                "GROUP BY tor.type_of_room " +
                "ORDER BY income DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(new RoomIncome(
                        rs.getString("type_of_room"),
                        rs.getInt("bookings_count"),
                        rs.getDouble("income")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Метод для получения дохода по типам услуг
    public static List<ServiceIncome> getServiceIncome() {
        List<ServiceIncome> result = new ArrayList<>();

        String sql = "SELECT s.service_type, COUNT(r.reservation_id) as bookings_count, " +
                "SUM(p.payment_amount) as income " +
                "FROM payment p " +
                "JOIN reservation r ON p.reservation_id = r.reservation_id " +
                "JOIN service s ON r.service_id = s.service_id " +
                "GROUP BY s.service_type " +
                "ORDER BY income DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(new ServiceIncome(
                        rs.getString("service_type"),
                        rs.getInt("bookings_count"),
                        rs.getDouble("income")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Вспомогательный метод для тестирования подключения
    public static boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}