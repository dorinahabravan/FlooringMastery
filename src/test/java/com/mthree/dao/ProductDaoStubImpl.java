package com.mthree.dao;

import com.mthree.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoStubImpl implements ProductDao{
    /**
     * Returns test product data.
     */
    @Override
    public List<Product> getAllProducts() {

        Product product = new Product();

        product.setProductType("Tile");
        product.setCostPerSquareFoot(new BigDecimal("3.50"));
        product.setLaborCostPerSquareFoot(new BigDecimal("4.15"));

        List<Product> products = new ArrayList<>();
        products.add(product);

        return products;
    }
}
