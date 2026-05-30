package com.library.librarymanagementsystem.models;

public class Member {
    private int    id;
    private String fullName;
    private String email;
    private String phone;
    private String address;

    public Member() {}

    public Member(int id, String fullName, String email, String phone, String address) {
        this.id       = id;
        this.fullName = fullName;
        this.email    = email;
        this.phone    = phone;
        this.address  = address;
    }

    // Getters & Setters
    public int    getId()                        { return id; }
    public void   setId(int id)                  { this.id = id; }

    public String getFullName()                  { return fullName; }
    public void   setFullName(String fullName)   { this.fullName = fullName; }

    public String getEmail()                     { return email; }
    public void   setEmail(String email)         { this.email = email; }

    public String getPhone()                     { return phone; }
    public void   setPhone(String phone)         { this.phone = phone; }

    public String getAddress()                   { return address; }
    public void   setAddress(String address)     { this.address = address; }
}
