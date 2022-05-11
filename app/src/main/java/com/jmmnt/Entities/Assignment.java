package com.company.Entity;

public class Assignment {
    private Customer customer;
    private User user;
    private String orderNumber;
    private String address;
    private String addressNumber;
    private String postalCode;
    private String city;
    //TODO skal laves felter tjeklisten... Ikke 100% aftalt hvordan endnu.


    public Assignment(Customer customer, User user, String orderNumber, String address, String adressNumber, String postalCode, String city) {
        this.customer = customer;
        this.user = user;
        this.orderNumber = orderNumber;
        this.address = address;
        this.addressNumber = adressNumber;
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

    public String getAddressNumber() {
        return addressNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }
}
