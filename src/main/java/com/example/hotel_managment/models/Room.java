package com.example.hotel_managment.models;

public class Room {
    Integer id, room_number;

    String type_of_room, room_status;

    public Room(Integer id, Integer room_number, String type_of_room, String room_status) {
        this.id = id;
        this.room_number = room_number;
        this.type_of_room = type_of_room;
        this.room_status = room_status;
    }

    public Room() {

    }

    public Integer getId() {
        return id;
    }

    public Integer getRoom_number() {
        return room_number;
    }

    public String getType_of_room() {
        return type_of_room;
    }

    public String getRoom_status() {
        return room_status;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoom_number(Integer room_number) {
        this.room_number = room_number;
    }

    public void setType_of_room(String type_of_room) {
        this.type_of_room = type_of_room;
    }

    public void setRoom_status(String room_status) {
        this.room_status = room_status;
    }
}
