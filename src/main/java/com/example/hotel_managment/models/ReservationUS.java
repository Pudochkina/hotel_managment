package com.example.hotel_managment.models;

import java.sql.Date;

public class ReservationUS {
    Integer reservation_id, guest_id, total_sum;
    String guest_fio, service_name, room_number, type_of_room;
    Date date_in, date_out;

    public ReservationUS(Integer reservation_id, Integer guest_id, String guest_fio, String service_name, String room_number, String type_of_room, Date date_in, Date date_out,  Integer total_sum) {
        this.reservation_id = reservation_id;
        this.guest_id = guest_id;
        this.guest_fio = guest_fio;
        this.service_name = service_name;
        this.room_number = room_number;
        this.type_of_room = type_of_room;
        this.date_in = date_in;
        this.date_out = date_out;
        this.total_sum = total_sum;
    }

    public ReservationUS() {
    }

    public Integer getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(Integer reservation_id) {
        this.reservation_id = reservation_id;
    }

    public Integer getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(Integer guest_id) {
        this.guest_id = guest_id;
    }

    public Integer getTotal_sum() {
        return total_sum;
    }

    public void setTotal_sum(Integer total_sum) {
        this.total_sum = total_sum;
    }

    public String getGuest_fio() {
        return guest_fio;
    }

    public void setGuest_fio(String guest_fio) {
        this.guest_fio = guest_fio;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getType_of_room() {
        return type_of_room;
    }

    public void setType_of_room(String type_of_room) {
        this.type_of_room = type_of_room;
    }

    public Date getDate_in() {
        return date_in;
    }

    public void setDate_in(Date date_in) {
        this.date_in = date_in;
    }

    public Date getDate_out() {
        return date_out;
    }

    public void setDate_out(Date date_out) {
        this.date_out = date_out;
    }
}
