package com.example.cityxpertzz.models;

public class BookingModel {
    public String bookingId;
    public String userId;
    public String userName;
    public String providerId;
    public String providerName;
    public String serviceId;
    public String serviceTitle;
    public double price;
    public String status; // "Pending", "Accepted", "Completed", "Cancelled"
    public long timestamp; // booking time (System.currentTimeMillis())
    public String userPhone;
    public String providerPhone;

    public BookingModel() { }

    public BookingModel(String bookingId, String userId, String userName,
                        String providerId, String providerName, String serviceId,
                        String serviceTitle, double price, String status,
                        long timestamp, String userPhone, String providerPhone) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.userName = userName;
        this.providerId = providerId;
        this.providerName = providerName;
        this.serviceId = serviceId;
        this.serviceTitle = serviceTitle;
        this.price = price;
        this.status = status;
        this.timestamp = timestamp;
        this.userPhone = userPhone;
        this.providerPhone = providerPhone;
    }

    @Override
    public String toString() {
        return "BookingModel{" +
                "bookingId='" + bookingId + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", providerId='" + providerId + '\'' +
                ", providerName='" + providerName + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", serviceTitle='" + serviceTitle + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", userPhone='" + userPhone + '\'' +
                ", providerPhone='" + providerPhone + '\'' +
                '}';
    }
}
