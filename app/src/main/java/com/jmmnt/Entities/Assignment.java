package com.jmmnt.Entities;

import java.sql.Date;
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

    private String installedBy;
    private String verifiedBy;
    private String identificationOfInstallation;

    public Assignment(int assignmentId, int foremanId, String address, String postalCode,
                      String status, String orderNumber, LocalDate statusDate, String customerName) {
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

    public Assignment(int assignment_id, String customer_name, String order_number, String address, String postal_code, String city, String status, LocalDate status_date) {
        this.assignmentId = assignment_id;
        this.customerName = customer_name;
        this.orderNumber = order_number;
        this.address = address;
        this.postalCode = postal_code;
        this.city = city;
        this.status = status;
        this.statusDate = status_date;
        }

    @Override
        public String toString () {
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

        public int getAssignmentId () {
            return assignmentId;
        }

        public void setAssignmentId ( int assignmentId){
            this.assignmentId = assignmentId;
        }

        public int getUserID () {
            return userID;
        }

        public void setUserID ( int userID){
            this.userID = userID;
        }

        public String getAddress () {
            return address;
        }

        public void setAddress (String address){
            this.address = address;
        }

        public String getPostalCode () {
            return postalCode;
        }

        public void setPostalCode (String postalCode){
            this.postalCode = postalCode;
        }

        public String getStatus () {
            return status;
        }

        public void setStatus (String status){
            this.status = status;
        }

        public String getOrderNumber () {
            return orderNumber;
        }

        public void setOrderNumber (String orderNumber){
            this.orderNumber = orderNumber;
        }

        public String getCustomerName () {
            return customerName;
        }

        public void setCustomerName (String customerName){
            this.customerName = customerName;
        }

        public LocalDate getStatusDate () {
            return statusDate;
        }

        public void setStatusDate (LocalDate statusDate){
            this.statusDate = statusDate;
        }

        public String getCity () {
            return city;
        }

        public void setCity (String city){
            this.city = city;
        }

        public String getInstalledBy () {
            return installedBy;
        }

        public void setInstalledBy (String installedBy){
            this.installedBy = installedBy;
        }

        public String getVerifiedBy () {
            return verifiedBy;
        }

        public void setVerifiedBy (String verifiedBy){
            this.verifiedBy = verifiedBy;
        }

        public String getIdentificationOfInstallation () {
            return identificationOfInstallation;
        }

        public void setIdentificationOfInstallation (String identificationOfInstallation){
            this.identificationOfInstallation = identificationOfInstallation;
        }
    }
