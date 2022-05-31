package com.jmmnt.Entities;

import org.apache.commons.net.ntp.TimeStamp;

import java.time.LocalDate;

public class Assignment {
    private int assignmentId;
    private int userID;
    private String address;
    private String postalCode;
    private String status;
    private String orderNumber;
    private TimeStamp finishedOn;
    private String customerName;
    private LocalDate statusDate;

    public Assignment(int assignmentId, int foremanId, String address, String postalCode,
                      String status, String orderNumber, TimeStamp finishedOn, String customerName) {
        this.assignmentId = assignmentId;
        this.userID = foremanId;
        this.address = address;
        this.postalCode = postalCode;
        this.status = status;
        this.orderNumber = orderNumber;
        this.finishedOn = finishedOn;
        this.customerName = customerName;
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

    public TimeStamp getFinishedOn() {
        return finishedOn;
    }

    public void setFinishedOn(TimeStamp finishedOn) {
        this.finishedOn = finishedOn;
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
}
