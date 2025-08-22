package com.example.hotel_managment.models;

public class ServiceIncome {
    private final String serviceType;
    private final int bookingsCount;
    private final double income;

    public ServiceIncome(String serviceType, int bookingsCount, double income) {
        this.serviceType = serviceType;
        this.bookingsCount = bookingsCount;
        this.income = income;
    }

    public String getServiceType() { return serviceType; }
    public int getBookingsCount() { return bookingsCount; }
    public double getIncome() { return income; }
}
