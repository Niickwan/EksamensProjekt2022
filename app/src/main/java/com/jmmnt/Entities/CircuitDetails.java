package com.jmmnt.Entities;

public class CircuitDetails {

    private String groupName;
    private String ob;
    private String characteristics;
    private String crossSection;
    private String maxOB;
    private int checkbox;
    private String omega;
    private String megaOmega;

    public CircuitDetails(String groupName, String ob, String characteristics, String crossSection, String maxOB, int checkbox, String omega, String megaOmega) {
        this.groupName = groupName;
        this.ob = ob;
        this.characteristics = characteristics;
        this.crossSection = crossSection;
        this.maxOB = maxOB;
        this.checkbox = checkbox;
        this.omega = omega;
        this.megaOmega = megaOmega;
    }

    public CircuitDetails() {
        this.groupName = "";
        this.ob = "";
        this.characteristics = "";
        this.crossSection = "";
        this.maxOB = "";
        this.checkbox = -1;
        this.omega = "";
        this.megaOmega = "";
    }

    @Override
    public String toString() {
        return "Results{" +
                "groupName='" + groupName + '\'' +
                ", ob='" + ob + '\'' +
                ", characteristics='" + characteristics + '\'' +
                ", crossSection='" + crossSection + '\'' +
                ", maxOB='" + maxOB + '\'' +
                ", checkbox=" + checkbox +
                ", omega='" + omega + '\'' +
                ", megaOmega='" + megaOmega + '\'' +
                '}';
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOb() {
        return ob;
    }

    public void setOb(String ob) {
        this.ob = ob;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public String getCrossSection() {
        return crossSection;
    }

    public void setCrossSection(String crossSection) {
        this.crossSection = crossSection;
    }

    public String getMaxOB() {
        return maxOB;
    }

    public void setMaxOB(String maxOB) {
        this.maxOB = maxOB;
    }

    public int getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(int checkbox) {
        this.checkbox = checkbox;
    }

    public String getOmega() {
        return omega;
    }

    public void setOmega(String omega) {
        this.omega = omega;
    }

    public String getMegaOmega() {
        return megaOmega;
    }

    public void setMegaOmega(String megaOmega) {
        this.megaOmega = megaOmega;
    }
}
