package com.mthree.dao;

import com.mthree.model.Tax;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

public class TaxDaoFileImpl implements  TaxDao {


    private Map <String, Tax>  allTaxes = new HashMap<>();
    private static  final String DELIMITER = ",";
    private static final String TAX_FILE = "Data/Taxes.txt";

    /**
     * Loads tax information from the tax file into memory
     */
    private void loadFile(){

        allTaxes.clear();
        try (Scanner scanner = new Scanner(new File(TAX_FILE))){

            // Skip the header row
            if(scanner.hasNextLine()) {
                scanner.nextLine();
            }

            //Read and convert each remaining row into a Tax object
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] values = line.split(DELIMITER);

                Tax tax = new Tax();

                tax.setStateAbr(values[0]);
                tax.setState(values[1]);
                tax.setTaxRate(new BigDecimal(values[2]));

                allTaxes.put(tax.getStateAbr(), tax);
            }

        }catch (FileNotFoundException e){
            throw new RuntimeException("Tax file could not be found.", e);
        }

    }

    /**
     * Returns all available tax records
     * @return a list containing all taxes
     */
    @Override
    public List<Tax> getAllTaxes() {
        loadFile();
        return new ArrayList<>(allTaxes.values());
    }


    }

