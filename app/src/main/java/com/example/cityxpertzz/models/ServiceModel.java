package com.example.cityxpertzz.models;

public class ServiceModel {
    public String serviceId;
    public String providerId;
    public String providerName;
    public String category;   // e.g., "Plumbing"
    public String title;      // e.g., "Tap Fixing"
    public String description;
    public double price;      // Example: 500.0
    public String imageUrl;   // Optional
    public boolean available; // true/false

    public ServiceModel() { }

    public ServiceModel(String serviceId, String providerId, String providerName, String category,
                        String title, String description, double price, String imageUrl, boolean available) {
        this.serviceId = serviceId;
        this.providerId = providerId;
        this.providerName = providerName;
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.available = available;
    }

    @Override
    public String toString() {
        return "ServiceModel{" +
                "serviceId='" + serviceId + '\'' +
                ", providerId='" + providerId + '\'' +
                ", providerName='" + providerName + '\'' +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", available=" + available +
                '}';
    }
}
