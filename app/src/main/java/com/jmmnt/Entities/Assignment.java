package com.jmmnt.Entities;

import org.apache.commons.net.ntp.TimeStamp;

public class Assignment {
    private int assignmentId;
    private int foremanId;
    private String address;
    private String postalCode;
    private String status;
    private String orderNumber;
    private TimeStamp finishedOn;
    private String customerName;

    public Assignment(int assignmentId, int foremanId, String address, String postalCode,
                      String status, String orderNumber, TimeStamp finishedOn, String customerName) {
        this.assignmentId = assignmentId;
        this.foremanId = foremanId;
        this.address = address;
        this.postalCode = postalCode;
        this.status = status;
        this.orderNumber = orderNumber;
        this.finishedOn = finishedOn;
        this.customerName = customerName;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getForemanId() {
        return foremanId;
    }

    public void setForemanId(int foremanId) {
        this.foremanId = foremanId;
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
}
