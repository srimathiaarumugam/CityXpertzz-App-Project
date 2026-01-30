package com.example.cityxpertzz.models;

public class UserModel {
    public String uid;
    public String name;
    public String email;
    public String phone;
    public String userType; // "user", "provider", or "admin"
    public boolean approved; // For providers: admin approval required

    public UserModel() {
        // Required empty constructor for Firebase
    }

    public UserModel(String uid, String name, String email, String phone, String userType, boolean approved) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", userType='" + userType + '\'' +
                ", approved=" + approved +
                '}';
    }
}
