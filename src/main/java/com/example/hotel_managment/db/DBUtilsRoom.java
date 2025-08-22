package com.example.hotel_managment.db;

import com.example.hotel_managment.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class DBUtilsRoom {

    /**
     * считывание из бд данных о комнатах
     */
    public static ObservableList<Room> getRoomFromDB()  {
        ObservableList<Room> roomSearchObservableList = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement getRoom = null;
        ResultSet queryOutput = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            getRoom = connection.prepareStatement("SELECT room_id, room_number, type_of_room, room_status FROM room ORDER BY room_number ASC");
            queryOutput = getRoom.executeQuery();

            while (queryOutput.next()) {
                Integer queryRoomId = queryOutput.getInt("room_id");
                Integer queryRoomNumber = queryOutput.getInt("room_number");
                String queryRoomType = queryOutput.getString("type_of_room");
                String queryRoomStatus = queryOutput.getString("room_status");

                roomSearchObservableList.add(new Room(queryRoomId, queryRoomNumber, queryRoomType, queryRoomStatus));
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
        return roomSearchObservableList;
    }

    /**
     * метод отвечающий за добавление нового гостя в бд
     */
    public static boolean addNewRoom(Integer number, String type, String status) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckGuestExist = null;
        ResultSet resultSet = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            psCheckGuestExist = connection.prepareStatement("SELECT * FROM room WHERE room_number = ? AND type_of_room = ?");
            psCheckGuestExist.setInt(1, number);
            psCheckGuestExist.setString(2, type);
            resultSet = psCheckGuestExist.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("Room already exists!");
                return false;
            } else {
                psInsert = connection.prepareStatement("INSERT INTO " +
                        "room (room_number, type_of_room, room_status) VALUES (?, ?, ?)");
                psInsert.setInt(1, number);
                psInsert.setString(2, type);
                psInsert.setString(3, status);
                psInsert.executeUpdate();
                System.out.println("Room successfully added!");
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
    public static boolean updateRoom(Integer id, Integer number, String type, String status) throws SQLException {
        Connection connection = null;
        PreparedStatement updateRoom = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            updateRoom = connection.prepareStatement("UPDATE room SET room_number = ?, type_of_room = ?, room_status = ? WHERE room_id = ?");
            updateRoom.setInt(1, number);
            updateRoom.setString(2, type);
            updateRoom.setString(3, status);
            updateRoom.setInt(4, id);
            int rowsAffected = updateRoom.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected);
            System.out.println("Room updated added!");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * метод отвечающий за удаление гостя из бд
     */
    public static boolean deleteRoom(Integer id) throws SQLException {
        Connection connection = null;
        PreparedStatement deleteGuest = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            deleteGuest = connection.prepareStatement("DELETE FROM room WHERE room_id = ?");
            deleteGuest.setInt(1, id);
            deleteGuest.executeUpdate();

            System.out.println("Room deleted!");
        } catch (SQLIntegrityConstraintViolationException ex) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Комнату нельзя удалить!");
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
     * метод отвечающий за добавление нового гостя в бд
     */
    public static boolean addNewTypeOfRoom(String name, Integer capacity, Double cost, String descr) throws SQLException {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckServiceExist = null;
        ResultSet resultSet = null;
        boolean success = false;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            psCheckServiceExist = connection.prepareStatement("SELECT * FROM type_of_room WHERE type_of_room = ?");
            psCheckServiceExist.setString(1, name);
            resultSet = psCheckServiceExist.executeQuery();
            if (resultSet.isBeforeFirst()) {
                System.out.println("Type of room already exists!");
                return false;
            } else {
                psInsert = connection.prepareStatement("INSERT INTO " +
                        "type_of_room (type_of_room, capacity, cost_per_night, description) VALUES (?, ?, ?, ?)");
                psInsert.setString(1, name);
                psInsert.setInt(2, capacity);
                psInsert.setDouble(3, cost);
                psInsert.setString(4, descr);
                psInsert.executeUpdate();
                System.out.println("Type of room successfully added!");
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
    public static boolean updateTypeOfRoom(String name, Integer capacity, Double cost, String descr) throws SQLException {
        Connection connection = null;
        PreparedStatement updateService = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            updateService = connection.prepareStatement("UPDATE type_of_room SET capacity = ?, cost_per_night = ?, description = ? " +
                    "WHERE type_of_room = ?");
            updateService.setInt(1, capacity);
            updateService.setDouble(2, cost);
            updateService.setString(3, descr);
            updateService.setString(4, name);
            int rowsAffected = updateService.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected);
            System.out.println("Type of room updated added!");
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating type of room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * метод отвечающий за удаление типа номера из бд
     */
    public static boolean deleteTypeOfRoom (String id) throws SQLException {
        Connection connection = null;
        PreparedStatement deleteType = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_managment_system", "root", "qwerty1234");
            deleteType = connection.prepareStatement("DELETE FROM type_of_room WHERE type_of_room = ?");
            deleteType.setString(1, id);
            deleteType.executeUpdate();

            System.out.println("Type of room deleted!");
        } catch (SQLIntegrityConstraintViolationException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Тип номера нельзя удалить!");
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
            if (deleteType != null) {
                try {
                    deleteType.close();
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
        if (deleteType.isClosed()) {
            return true;
        } else {
            return false;
        }
    }

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
            getTypeOfRoom = connection.prepareStatement("SELECT type_of_room, capacity,"
                    + " cost_per_night, description FROM type_of_room");
            queryOutput = getTypeOfRoom.executeQuery();

            while (queryOutput.next()) {
                String queryTypeOfRoomId = queryOutput.getString("type_of_room");
                Integer queryTypeOfRoomCapacity = queryOutput.getInt("capacity");
                Integer queryTypeOfRoomCost = queryOutput.getInt("cost_per_night");
                String queryTypeOfRoomDescr = queryOutput.getString("description");


                TypeOfRoomSearchObservableList.add(new TypeOfRoom(queryTypeOfRoomId, queryTypeOfRoomDescr, queryTypeOfRoomCapacity, queryTypeOfRoomCost));
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
            String sql = "SELECT r.room_id, r.room_number, r.type_of_room, r.room_status,"
                    + " rt.capacity, rt.cost_per_night, rt.description " +
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

