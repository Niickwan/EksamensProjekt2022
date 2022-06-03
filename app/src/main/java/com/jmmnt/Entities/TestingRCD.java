package com.jmmnt.Entities;

public class TestingRCD {

    private String groupName;
    private int checkboxOK;
    private String firstResult;
    private String secondResult;
    private String thirdResult;
    private String fourthResult;
    private String fifthResult;
    private String sixthResult;

    public TestingRCD(String groupName, int checkboxOK, String firstResult, String secondResult, String thirdResult, String fourthResult, String fifthResult, String sixthResult) {
        this.groupName = groupName;
        this.checkboxOK = checkboxOK;
        this.firstResult = firstResult;
        this.secondResult = secondResult;
        this.thirdResult = thirdResult;
        this.fourthResult = fourthResult;
        this.fifthResult = fifthResult;
        this.sixthResult = sixthResult;
    }

    public TestingRCD(){
        this.groupName = "";
        this.checkboxOK = -1;
        this.firstResult = "";
        this.secondResult = "";
        this.thirdResult = "";
        this.fourthResult = "";
        this.fifthResult = "";
        this.sixthResult = "";
    }

    @Override
    public String toString() {
        return "TestingRCD{" +
                "groupName='" + groupName + '\'' +
                ", checkboxOK=" + checkboxOK +
                ", firstResult='" + firstResult + '\'' +
                ", secondResult='" + secondResult + '\'' +
                ", thirdResult='" + thirdResult + '\'' +
                ", fourthResult='" + fourthResult + '\'' +
                ", fifthResult='" + fifthResult + '\'' +
                ", sixthResult='" + sixthResult + '\'' +
                '}';
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCheckboxOK() {
        return checkboxOK;
    }

    public void setCheckboxOK(int checkboxOK) {
        this.checkboxOK = checkboxOK;
    }

    public String getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(String firstResult) {
        this.firstResult = firstResult;
    }

    public String getSecondResult() {
        return secondResult;
    }

    public void setSecondResult(String secondResult) {
        this.secondResult = secondResult;
    }

    public String getThirdResult() {
        return thirdResult;
    }

    public void setThirdResult(String thirdResult) {
        this.thirdResult = thirdResult;
    }

    public String getFourthResult() {
        return fourthResult;
    }

    public void setFourthResult(String fourthResult) {
        this.fourthResult = fourthResult;
    }

    public String getFifthResult() {
        return fifthResult;
    }

    public void setFifthResult(String fifthResult) {
        this.fifthResult = fifthResult;
    }

    public String getSixthResult() {
        return sixthResult;
    }

    public void setSixthResult(String sixthResult) {
        this.sixthResult = sixthResult;
    }
}
