package com.mthree.dao;

import com.mthree.model.Tax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxDaoStubImpl implements TaxDao{
    /**
     * Returns test tax data.
     */
    @Override
    public List<Tax> getAllTaxes() {

        Tax tax = new Tax();

        tax.setState("Texas");
        tax.setStateAbr("TX");
        tax.setTaxRate(new BigDecimal("4.45"));

        List<Tax> taxes = new ArrayList<>();
        taxes.add(tax);

        return taxes;
    }
}
