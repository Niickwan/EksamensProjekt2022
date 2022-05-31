package com.jmmnt.Entities;

import android.icu.util.LocaleData;

import org.apache.commons.net.ntp.TimeStamp;

import java.time.LocalDate;

public class Assignment {
    private int assignmentId;
    private int userID;
    private String address;
    private String postalCode;
    private String city;
    private String status;
    private String orderNumber;
    private String customerName;
    private LocalDate statusDate;

    public Assignment(int assignmentId, int foremanId, String address, String postalCode,
                      String status, String orderNumber,LocalDate statusDate, String customerName) {
        this.assignmentId = assignmentId;
        this.userID = foremanId;
        this.address = address;
        this.postalCode = postalCode;
        this.status = status;
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.statusDate = statusDate;
    }

    public Assignment(int assignmentId, String address, String postalCode, String status, String orderNumber, LocalDate statusDate, String customerName, int userID) {
        this.assignmentId = assignmentId;
        this.address = address;
        this.postalCode = postalCode;
        this.status = status;
        this.orderNumber = orderNumber;
        this.statusDate = statusDate;
        this.customerName = customerName;
        this.userID = userID;
    }

    public Assignment(String orderNumber, String customerName, String address, String postalCode, String city, LocalDate statusDate, String status, int userID) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.statusDate = statusDate;
        this.status = status;
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentId=" + assignmentId +
                ", userID=" + userID +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", status='" + status + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", statusDate=" + statusDate +
                '}';
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(LocalDate statusDate) {
        this.statusDate = statusDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
