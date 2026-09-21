package com.mthree.dao;

import com.mthree.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoFileImplTest {

    /**
     * Tests that products are loaded from the product file.
     */
    @Test
    public void testGetAllProducts() {

        // ARRANGE
        ProductDao productDao = new ProductDaoFileImpl();

        // ACT
        List<Product> products = productDao.getAllProducts();

        // ASSERT
        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    /**
     * Tests that product data is read correctly from the file.
     */
    @Test
    public void testProductData() {

        // ARRANGE
        ProductDao productDao = new ProductDaoFileImpl();

        // ACT
        List<Product> products = productDao.getAllProducts();

        Product tile = products.stream()
                .filter(product -> product.getProductType().equalsIgnoreCase("Tile"))
                .findFirst()
                .orElse(null);

        // ASSERT
        assertNotNull(tile);
        assertEquals(new BigDecimal("3.50"), tile.getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.15"), tile.getLaborCostPerSquareFoot());
}
}