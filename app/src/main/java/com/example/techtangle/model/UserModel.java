package com.example.techtangle.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String name ;
    private String username;
    private String email;
    private String dob;
    private String profession ;
    private String password;


    public UserModel() {
    }

    public UserModel(String name, String username, String email, String dob, String profession, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.dob = dob;
        this.profession = profession;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}