package com.example.hotel_managment.db;

import com.example.hotel_managment.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DBUtilsRoom {

    /**
     * метод отвечающий за поиск id комнаты по ее номеру
     */
    public static Integer getRoomIdByRoomNumber(Integer room_number) {
        Integer p_id = null;

        Connection connection = null;
        PreparedStatement getRoomId = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            getRoomId = connection.prepareStatement("SELECT room_id FROM room WHERE room_number = ?");
            getRoomId.setInt(1, room_number);
            queryOutput = getRoomId.executeQuery();

            while (queryOutput.next()) {
                Integer queryProductId = queryOutput.getInt("room_id");
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
            if (getRoomId != null) {
                try {
                    getRoomId.close();
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
     * считывание из бд данных о типах номеров
     */
    public static ObservableList<TypeOfRoom> getTypesOfRoomsFromDB() {
        ObservableList<TypeOfRoom> TypeOfRoomSearchObservableList = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement getTypeOfRoom = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            getTypeOfRoom = connection.prepareStatement("SELECT type_of_room, capacity, cost_per_night, description FROM type_of_room");
            queryOutput = getTypeOfRoom.executeQuery();

            while (queryOutput.next()) {
                String queryTypeOfRoomId = queryOutput.getString("type_of_room");
                Integer queryTypeOfRoomCapacity = queryOutput.getInt("capacity");
                Integer queryTypeOfRoomCost = queryOutput.getInt("cost_per_night");
                String queryTypeOfRoomDescr = queryOutput.getString("description");


                TypeOfRoomSearchObservableList.add(new TypeOfRoom(queryTypeOfRoomId, queryTypeOfRoomDescr, queryTypeOfRoomCost, queryTypeOfRoomCapacity));
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
            if (getTypeOfRoom != null) {
                try {
                    getTypeOfRoom.close();
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
        return TypeOfRoomSearchObservableList;
    }

    /**
     * считывание из бд данных о номерах после фильтрации по дате типу статусу
     */
    public static ObservableList<Room> getFilteredNumberOfRoomFromDB(Date checkInDate, Date checkOutDate, String roomType) {
        ObservableList<Room> RoomSearchObservableList = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement getRoom = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            String sql = "SELECT r.room_id, r.room_number, r.type_of_room, r.room_status, rt.capacity, rt.cost_per_night, rt.description " +
                    "FROM room r " +
                    "JOIN type_of_room rt ON r.type_of_room = rt.type_of_room " +
                    "WHERE rt.type_of_room = ? " +
                    "AND r.room_id NOT IN (" +
                    "    SELECT DISTINCT room_id " +
                    "    FROM reservation " +
                    "    WHERE reservation_status IN ('Подтверждено', 'Ожидает оплаты') " +
                    "    AND (date_in < ? AND date_out > ?)" +
                    ") " +
                    "AND r.room_status = 'Свободен'";

            getRoom = connection.prepareStatement(sql);

            getRoom.setString(1, roomType);
            getRoom.setDate(2, checkOutDate);
            getRoom.setDate(3, checkInDate);

            queryOutput = getRoom.executeQuery();

            while (queryOutput.next()) {
                Room room = new Room();
                room.setId(queryOutput.getInt("room_id"));
                room.setRoom_number(queryOutput.getInt("room_number"));
                room.setType_of_room(queryOutput.getString("type_of_room"));
                room.setRoom_status(queryOutput.getString("room_status"));
                RoomSearchObservableList.add(room);
                System.out.println(room.getRoom_number());
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
            if (getRoom != null) {
                try {
                    getRoom.close();
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
        return RoomSearchObservableList;
    }

    /**
     * считывание из бд данных о вместимости и стоимости номеров после фильтрации по дате типу статусу
     */
    public static ObservableList<TypeOfRoom> getFilteredTypeOfRoomFromDB(Date checkInDate, Date checkOutDate, String roomType) {
        ObservableList<TypeOfRoom> typeOfRoomsObservableList = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement getRoom = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            String sql = "SELECT r.room_id, r.room_number, r.type_of_room, r.room_status, rt.capacity, rt.cost_per_night, rt.description " +
                    "FROM room r " +
                    "JOIN type_of_room rt ON r.type_of_room = rt.type_of_room " +
                    "WHERE rt.type_of_room = ? " +
                    "AND r.room_id NOT IN (" +
                    "    SELECT DISTINCT room_id " +
                    "    FROM reservation " +
                    "    WHERE reservation_status IN ('Подтверждено', 'Ожидает оплаты') " +
                    "    AND (date_in < ? AND date_out > ?)" +
                    ") " +
                    "AND r.room_status = 'Свободен'";

            getRoom = connection.prepareStatement(sql);

            getRoom.setString(1, roomType);
            getRoom.setDate(2, checkOutDate);
            getRoom.setDate(3, checkInDate);

            queryOutput = getRoom.executeQuery();

            while (queryOutput.next()) {
                TypeOfRoom typeOfRoom = new TypeOfRoom();
                typeOfRoom.setType_of_room(queryOutput.getString("type_of_room"));
                typeOfRoom.setCapacity(queryOutput.getInt("capacity"));
                typeOfRoom.setCost_per_night(queryOutput.getInt("cost_per_night"));
                typeOfRoom.setDescription(queryOutput.getString("description"));
                typeOfRoomsObservableList.add(typeOfRoom);
                System.out.println(typeOfRoom.getCapacity());
                System.out.println(typeOfRoom.getCost_per_night());
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
            if (getRoom != null) {
                try {
                    getRoom.close();
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
        return typeOfRoomsObservableList;
    }

    public static Double getRoomPriceByType(String roomType) {
        Connection connection = null;
        PreparedStatement getRoomId = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            getRoomId = connection.prepareStatement("SELECT cost_per_night FROM type_of_room WHERE type_of_room = ?");
            getRoomId.setString(1, roomType);
            queryOutput = getRoomId.executeQuery();

            while (queryOutput.next()) {
                return queryOutput.getDouble("cost_per_night");
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
            if (getRoomId != null) {
                try {
                    getRoomId.close();
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

