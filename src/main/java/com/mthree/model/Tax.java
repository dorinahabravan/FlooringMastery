package com.mthree.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Tax {


    private  String state;
    private  String stateAbr;
    private BigDecimal taxRate;

    public String getState() {
        return state;
    }

    public String getStateAbr() {
        return stateAbr;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setStateAbr(String stateAbr) {
        this.stateAbr = stateAbr;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }


    /**
     * Compares tax records using the state abbreviation.
     * @param o   the reference object with which to compare.
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tax tax = (Tax) o;
        return Objects.equals(stateAbr, tax.stateAbr);
    }

    /**
     * Generates a hash code using the state abbreviation.
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(stateAbr);
    }


    /**
     * Returns the tax details as a String.
     * @return
     */
    @Override
    public String toString() {
        return "Tax{" +
                "state='" + state + '\'' +
                ", stateAbr='" + stateAbr + '\'' +
                ", taxRate=" + taxRate +
                '}';
    }
}
