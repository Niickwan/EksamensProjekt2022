package com.jmmnt.Entities;

public class ShortCircuitCurrentAndVoltageDrop {

    private String shortCircuitGroupName;
    private String shortCircuitLk;
    private String shortCircuitMeasuredOnLocation;
    private String voltageDropGroupName;
    private String voltageDropDeltaVoltage;
    private String voltageDropMeasuredOnLocation;

    public ShortCircuitCurrentAndVoltageDrop(String shortCircuitGroupName, String shortCircuitLk, String shortCircuitMeasuredOnLocation, String voltageDropGroupName, String voltageDropDeltaVoltage, String voltageDropMeasuredOnLocation) {
        this.shortCircuitGroupName = shortCircuitGroupName;
        this.shortCircuitLk = shortCircuitLk;
        this.shortCircuitMeasuredOnLocation = shortCircuitMeasuredOnLocation;
        this.voltageDropGroupName = voltageDropGroupName;
        this.voltageDropDeltaVoltage = voltageDropDeltaVoltage;
        this.voltageDropMeasuredOnLocation = voltageDropMeasuredOnLocation;
    }

    public ShortCircuitCurrentAndVoltageDrop(){
        this.shortCircuitGroupName = "";
        this.shortCircuitLk = "";
        this.shortCircuitMeasuredOnLocation = "";
        this.voltageDropGroupName = "";
        this.voltageDropDeltaVoltage = "";
        this.voltageDropMeasuredOnLocation = "";
    }

    @Override
    public String toString() {
        return "ShortCircuitAndVoltageDrop{" +
                "shortCircuitGroupName='" + shortCircuitGroupName + '\'' +
                ", shortCircuitLk='" + shortCircuitLk + '\'' +
                ", shortCircuitMeasuredOnLocation='" + shortCircuitMeasuredOnLocation + '\'' +
                ", voltageDropGroupName='" + voltageDropGroupName + '\'' +
                ", voltageDropDeltaVoltage='" + voltageDropDeltaVoltage + '\'' +
                ", voltageDropMeasuredOnLocation='" + voltageDropMeasuredOnLocation + '\'' +
                '}';
    }

    public String getShortCircuitGroupName() {
        return shortCircuitGroupName;
    }

    public void setShortCircuitGroupName(String shortCircuitGroupName) {
        this.shortCircuitGroupName = shortCircuitGroupName;
    }

    public String getShortCircuitLk() {
        return shortCircuitLk;
    }

    public void setShortCircuitLk(String shortCircuitLk) {
        this.shortCircuitLk = shortCircuitLk;
    }

    public String getShortCircuitMeasuredOnLocation() {
        return shortCircuitMeasuredOnLocation;
    }

    public void setShortCircuitMeasuredOnLocation(String shortCircuitMeasuredOnLocation) {
        this.shortCircuitMeasuredOnLocation = shortCircuitMeasuredOnLocation;
    }

    public String getVoltageDropGroupName() {
        return voltageDropGroupName;
    }

    public void setVoltageDropGroupName(String voltageDropGroupName) {
        this.voltageDropGroupName = voltageDropGroupName;
    }

    public String getVoltageDropDeltaVoltage() {
        return voltageDropDeltaVoltage;
    }

    public void setVoltageDropDeltaVoltage(String voltageDropDeltaVoltage) {
        this.voltageDropDeltaVoltage = voltageDropDeltaVoltage;
    }

    public String getVoltageDropMeasuredOnLocation() {
        return voltageDropMeasuredOnLocation;
    }

    public void setVoltageDropMeasuredOnLocation(String voltageDropMeasuredOnLocation) {
        this.voltageDropMeasuredOnLocation = voltageDropMeasuredOnLocation;
    }
}
