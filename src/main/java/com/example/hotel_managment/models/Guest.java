package com.example.hotel_managment.models;

import java.util.Date;

public class Guest {

    final Integer id;
    final String guestFIO, guest_phone,guest_email, guest_passport;
    final Date guest_date_birth;

    public Guest(Integer id, String product_name, String guest_phone, String guest_email, Date guest_date_birth, String guest_passport) {
        this.id = id;
        this.guestFIO = product_name;
        this.guest_phone = guest_phone;
        this.guest_email = guest_email;
        this.guest_date_birth = guest_date_birth;
        this.guest_passport = guest_passport;
    }

    public Integer getId() {
        return id;
    }

    public String getGuestFIO() {
        return guestFIO;
    }

    public String getGuest_phone() {
        return guest_phone;
    }

    public String getGuest_email() {
        return guest_email;
    }

    public String getGuest_passport() {
        return guest_passport;
    }

    public Date getGuest_date_birth() {
        return guest_date_birth;
    }


}
