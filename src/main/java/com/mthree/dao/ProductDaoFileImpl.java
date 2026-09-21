package com.mthree.dao;

import com.mthree.model.Product;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

@Component
public class ProductDaoFileImpl implements  ProductDao{


    private static final String DELIMITER = ",";
    private static final String PRODUCT_FILE = "Data/Products.txt";
    private Map <String, Product> allProducts = new HashMap<>();

    private void loadFile(){

        allProducts.clear();
        try( Scanner scanner = new Scanner (new File(PRODUCT_FILE))) {
            // Skip the header row
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }


            //Read and convert each remaining row into a Product object
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(DELIMITER);

                Product product = new Product();

                product.setProductType(values[0]);
                product.setCostPerSquareFoot(new BigDecimal(values[1]));
                product.setLaborCostPerSquareFoot(new BigDecimal(values[2]));

                allProducts.put(product.getProductType(), product);


            }

        }catch (FileNotFoundException e){
            throw new RuntimeException("Product file could not be found", e);
        }

    }

    /**
     * Returns all available product records
     * @return a list containing all products
     */
    @Override
    public List<Product> getAllProducts() {
        loadFile();
        return new ArrayList<>(allProducts.values());
    }


}
