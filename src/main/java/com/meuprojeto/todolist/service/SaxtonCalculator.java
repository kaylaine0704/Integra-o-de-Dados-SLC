package com.meuprojeto.todolist.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculator for Saxton & Rawls (1986) soil water retention model
 * Calculates soil properties from sand, clay and silt percentages
 */
public class SaxtonCalculator {

    private static final int SCALE = 6; // Decimal precision for BigDecimal

    /**
     * Result class containing all calculated soil properties
     */
    public static class SoilProperties {
        private BigDecimal fieldCapacity;
        private BigDecimal wiltingPoint;
        private BigDecimal availableWater;

        public SoilProperties(BigDecimal fieldCapacity, BigDecimal wiltingPoint, BigDecimal availableWater) {
            this.fieldCapacity = fieldCapacity;
            this.wiltingPoint = wiltingPoint;
            this.availableWater = availableWater;
        }

        public BigDecimal getFieldCapacity() { return fieldCapacity; }
        public BigDecimal getWiltingPoint() { return wiltingPoint; }
        public BigDecimal getAvailableWater() { return availableWater; }
    }

    /**
     * Calculate field capacity using Saxton formulas
     * @param sand Sand fraction (0-1)
     * @param clay Clay fraction (0-1)
     * @param silt Silt fraction (0-1)
     * @return Field capacity as BigDecimal
     */
    public static BigDecimal fieldCapacity(BigDecimal sand, BigDecimal clay, BigDecimal silt) {
        // -0.251 * sand + 0.195 * clay + 0.011 * silt + 0.006 * sand * clay - 0.027 * clay * silt + 0.452
        BigDecimal result = sand.multiply(new BigDecimal("-0.251"))
                              .add(clay.multiply(new BigDecimal("0.195")))
                              .add(silt.multiply(new BigDecimal("0.011")))
                              .add(sand.multiply(clay).multiply(new BigDecimal("0.006")))
                              .subtract(clay.multiply(silt).multiply(new BigDecimal("0.027")))
                              .add(new BigDecimal("0.452"));

        return result.setScale(SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculate wilting point using Saxton formulas
     * @param sand Sand fraction (0-1)
     * @param clay Clay fraction (0-1)
     * @param silt Silt fraction (0-1)
     * @return Wilting point as BigDecimal
     */
    public static BigDecimal wiltingPoint(BigDecimal sand, BigDecimal clay, BigDecimal silt) {
        // -0.024 * sand + 0.487 * clay + 0.006 * silt + 0.005 * sand * clay - 0.013 * clay * silt + 0.029
        BigDecimal result = sand.multiply(new BigDecimal("-0.024"))
                              .add(clay.multiply(new BigDecimal("0.487")))
                              .add(silt.multiply(new BigDecimal("0.006")))
                              .add(sand.multiply(clay).multiply(new BigDecimal("0.005")))
                              .subtract(clay.multiply(silt).multiply(new BigDecimal("0.013")))
                              .add(new BigDecimal("0.029"));

        return result.setScale(SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculate available water (field capacity - wilting point)
     * @param fieldCapacity Field capacity value
     * @param wiltingPoint Wilting point value
     * @return Available water as BigDecimal
     */
    public static BigDecimal availableWater(BigDecimal fieldCapacity, BigDecimal wiltingPoint) {
        return fieldCapacity.subtract(wiltingPoint).setScale(SCALE, RoundingMode.HALF_UP);
    }

    /**
     * Calculate soil properties using Saxton formulas
     * @param sandPercent Sand percentage (0-100)
     * @param clayPercent Clay percentage (0-100)
     * @param siltPercent Silt percentage (0-100)
     * @return SoilProperties object with calculated values
     */
    public static SoilProperties calculate(double sandPercent, double clayPercent, double siltPercent) {
        // Convert percentages to fractions (0-1)
        BigDecimal sand = new BigDecimal(sandPercent).divide(new BigDecimal("100"), SCALE, RoundingMode.HALF_UP);
        BigDecimal clay = new BigDecimal(clayPercent).divide(new BigDecimal("100"), SCALE, RoundingMode.HALF_UP);
        BigDecimal silt = new BigDecimal(siltPercent).divide(new BigDecimal("100"), SCALE, RoundingMode.HALF_UP);

        BigDecimal fc = fieldCapacity(sand, clay, silt);
        BigDecimal wp = wiltingPoint(sand, clay, silt);
        BigDecimal aw = availableWater(fc, wp);

        return new SoilProperties(fc, wp, aw);
    }

    /**
     * Calculate soil properties with silt = 100 - sand - clay
     * @param sandPercent Sand percentage (0-100)
     * @param clayPercent Clay percentage (0-100)
     * @return SoilProperties object with calculated values
     */
    public static SoilProperties calculate(double sandPercent, double clayPercent) {
        double siltPercent = 100.0 - sandPercent - clayPercent;
        if (siltPercent < 0) {
            siltPercent = 0.0; // Ensure non-negative
        }
        return calculate(sandPercent, clayPercent, siltPercent);
    }

    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        BigDecimal sand = new BigDecimal("0.45");  // 45% sand
        BigDecimal clay = new BigDecimal("0.30");  // 30% clay
        BigDecimal silt = new BigDecimal("0.25");  // 25% silt

        BigDecimal fieldCapacity = fieldCapacity(sand, clay, silt);
        BigDecimal wiltingPoint = wiltingPoint(sand, clay, silt);
        BigDecimal availableWater = availableWater(fieldCapacity, wiltingPoint);

        System.out.printf("Field capacity: %.3f%n", fieldCapacity);
        System.out.printf("Wilting point: %.3f%n", wiltingPoint);
        System.out.printf("Available water: %.3f%n", availableWater);
    }
}
