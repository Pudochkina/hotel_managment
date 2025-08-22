package com.example.hotel_managment.models;

public class RoomIncome {
    private final String roomType;
    private final int bookingsCount;
    private final double income;

    public RoomIncome(String roomType, int bookingsCount, double income) {
        this.roomType = roomType;
        this.bookingsCount = bookingsCount;
        this.income = income;
    }

    public String getRoomType() { return roomType; }
    public int getBookingsCount() { return bookingsCount; }
    public double getIncome() { return income; }
}
