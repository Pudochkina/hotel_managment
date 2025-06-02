package com.example.hotel_managment.models;

public class TypeOfRoom {
    String type_of_room, description;
    Integer capacity, cost_per_night;

    public TypeOfRoom(String type_of_room, String description, Integer capacity, Integer cost_per_night) {
        this.type_of_room = type_of_room;
        this.description = description;
        this.capacity = capacity;
        this.cost_per_night = cost_per_night;
    }

    public TypeOfRoom() {

    }

    public String getType_of_room() {
        return type_of_room;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getCost_per_night() {
        return cost_per_night;
    }

    public void setType_of_room(String type_of_room) {
        this.type_of_room = type_of_room;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setCost_per_night(Integer cost_per_night) {
        this.cost_per_night = cost_per_night;
    }
}
