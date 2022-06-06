package com.jmmnt.Entities;

import java.time.LocalDate;

public class Assignment {

    /**
     * Assignment keeps all the necessary information involving the entire assignment.
     */

    private int assignmentID;
    private int userID;
    private String address;
    private String postalCode;
    private String city;
    private String status;
    private String orderNumber;
    private String customerName;
    private LocalDate statusDate;
    private int installedBy;
    private int verifiedBy;
    private String identificationOfInstallation;

    public Assignment() {
    }

    public Assignment(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public Assignment(int assignmentID, String orderNumber, String customerName, String address, String postalCode, LocalDate statusDate, String status) {
        this.assignmentID = assignmentID;
        this.customerName = customerName;
        this.orderNumber = orderNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.status = status;
        this.statusDate = statusDate;
    }

    public Assignment(String orderNumber, String customerName, String address, String postalCode, String city, String identificationOfInstallation, int installedBy, int verifiedBy, LocalDate statusDate, String status) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.identificationOfInstallation = identificationOfInstallation;
        this.installedBy = installedBy;
        this.verifiedBy = verifiedBy;
        this.statusDate = statusDate;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentId=" + assignmentID +
                ", userID=" + userID +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", status='" + status + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", statusDate=" + statusDate +
                ", installedBy=" + installedBy +
                ", verifiedBy=" + verifiedBy +
                ", identificationOfInstallation='" + identificationOfInstallation + '\'' +
                '}';
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public int getUserID() {
        return userID;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDate getStatusDate() {
        return statusDate;
    }

    public void setVerifiedBy(int verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

}
