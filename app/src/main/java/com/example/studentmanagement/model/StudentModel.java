package com.example.studentmanagement.model;

import java.util.Date;

public class StudentModel {
    public int id;
    public String avaName, name, email, address, doB;

    public StudentModel(int id, String name, String email, String address, String dob){
        this.avaName = String.valueOf(name.charAt(0));
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.doB = dob;
    }
}
