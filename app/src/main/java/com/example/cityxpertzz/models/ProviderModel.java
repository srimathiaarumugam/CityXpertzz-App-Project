package com.example.cityxpertzz.models;

public class ProviderModel {
    public String uid;
    public String name;
    public String email;
    public String phone;
    public String category; // e.g., "Plumber", "Electrician"
    public String experience; // e.g., "3 years"
    public String address;
    public boolean approved; // Admin approval
    public double rating; // Optional future use

    public ProviderModel() { }

    public ProviderModel(String uid, String name, String email, String phone, String category,
                         String experience, String address, boolean approved, double rating) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.category = category;
        this.experience = experience;
        this.address = address;
        this.approved = approved;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ProviderModel{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", category='" + category + '\'' +
                ", experience='" + experience + '\'' +
                ", address='" + address + '\'' +
                ", approved=" + approved +
                ", rating=" + rating +
                '}';
    }
}
