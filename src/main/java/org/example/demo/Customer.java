package org.example.demo;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String address;
    private String cccd;

    public Customer(int id, String name, String phone, String address, String cccd) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.cccd = cccd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }
}