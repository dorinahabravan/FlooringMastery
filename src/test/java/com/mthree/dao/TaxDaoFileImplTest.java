package com.mthree.dao;

import com.mthree.model.Tax;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaxDaoFileImplTest {

    /**
     * Tests that taxes are loaded from the tax file.
     */
    @Test
    public void testGetAllTaxes() {

        // ARRANGE
        TaxDao taxDao = new TaxDaoFileImpl();

        // ACT
        List<Tax> taxes = taxDao.getAllTaxes();

        // ASSERT
        assertNotNull(taxes);
        assertFalse(taxes.isEmpty());
    }

    /**
     * Tests that tax data is read correctly from the file.
     */
    @Test
    public void testTaxData() {

        // ARRANGE
        TaxDao taxDao = new TaxDaoFileImpl();

        // ACT
        List<Tax> taxes = taxDao.getAllTaxes();

        Tax texas = taxes.stream()
                .filter(tax -> tax.getStateAbr().equalsIgnoreCase("TX"))
                .findFirst()
                .orElse(null);

        // ASSERT
        assertNotNull(texas);
        assertEquals("Texas", texas.getState());
        assertEquals(new BigDecimal("4.45"), texas.getTaxRate());
    }
}
