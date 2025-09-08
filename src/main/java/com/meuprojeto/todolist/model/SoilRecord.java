package com.meuprojeto.todolist.model;

public class SoilRecord {
    private String soilDataCode;
    private String soilDataName;
    private int upperDepth;
    private int lowerDepth;
    private double bulkDensity;
    private double fieldCapacity;
    private double wiltingPoint;
    private double evapCoeff;
    private double fractionOfRoots;
    private double sandFraction;
    private double clayFraction;
    private double orgMatterFraction;
    private double deltaMin;
    private double ksat;
    private double ph;
    private String attributes;

    public String getSoilDataCode() {
        return soilDataCode;
    }

    public void setSoilDataCode(String soilDataCode) {
        this.soilDataCode = soilDataCode;
    }

    public String getSoilDataName() {
        return soilDataName;
    }

    public void setSoilDataName(String soilDataName) {
        this.soilDataName = soilDataName;
    }

    public int getUpperDepth() {
        return upperDepth;
    }

    public void setUpperDepth(int upperDepth) {
        this.upperDepth = upperDepth;
    }

    public int getLowerDepth() {
        return lowerDepth;
    }

    public void setLowerDepth(int lowerDepth) {
        this.lowerDepth = lowerDepth;
    }

    public double getBulkDensity() {
        return bulkDensity;
    }

    public void setBulkDensity(double bulkDensity) {
        this.bulkDensity = bulkDensity;
    }

    public double getFieldCapacity() {
        return fieldCapacity;
    }

    public void setFieldCapacity(double fieldCapacity) {
        this.fieldCapacity = fieldCapacity;
    }

    public double getWiltingPoint() {
        return wiltingPoint;
    }

    public void setWiltingPoint(double wiltingPoint) {
        this.wiltingPoint = wiltingPoint;
    }

    public double getEvapCoeff() {
        return evapCoeff;
    }

    public void setEvapCoeff(double evapCoeff) {
        this.evapCoeff = evapCoeff;
    }

    public double getFractionOfRoots() {
        return fractionOfRoots;
    }

    public void setFractionOfRoots(double fractionOfRoots) {
        this.fractionOfRoots = fractionOfRoots;
    }

    public double getSandFraction() {
        return sandFraction;
    }

    public void setSandFraction(double sandFraction) {
        this.sandFraction = sandFraction;
    }

    public double getClayFraction() {
        return clayFraction;
    }

    public void setClayFraction(double clayFraction) {
        this.clayFraction = clayFraction;
    }

    public double getOrgMatterFraction() {
        return orgMatterFraction;
    }

    public void setOrgMatterFraction(double orgMatterFraction) {
        this.orgMatterFraction = orgMatterFraction;
    }

    public double getDeltaMin() {
        return deltaMin;
    }

    public void setDeltaMin(double deltaMin) {
        this.deltaMin = deltaMin;
    }

    public double getKsat() {
        return ksat;
    }

    public void setKsat(double ksat) {
        this.ksat = ksat;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
