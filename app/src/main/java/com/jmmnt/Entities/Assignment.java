package com.jmmnt.Entities;

public class Assignment {
    private Customer customer;
    private User user;
    private String orderNumber;
    private String address;
    private String postalCode;
    private String city;

    public Assignment(Customer customer, User user, String orderNumber, String address, String postalCode, String city) {
        this.customer = customer;
        this.user = user;
        this.orderNumber = orderNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
    }

    public Customer getCustomer() {
        return customer;
    }

    public User getUser() {
        return user;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }
}
