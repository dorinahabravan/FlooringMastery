package com.mthree.dao;

import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;
import org.springframework.stereotype.Component;

import javax.imageio.IIOException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;


@Component
public class ExportDaoFileImpl implements ExportDao{


    private static final String DELIMITER = ",";
    private static final String EXPORT_FILE = "Backup/DataExport.txt";

    /**
     * Writes all orders to the export file
     */
    private void writeToFile(Map<Integer , Order> orders) throws PersistenceException {

        try (PrintWriter writer = new PrintWriter(new FileWriter(EXPORT_FILE))){

            //Write the header row
            writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,"
                    + "CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,"
                    + "LaborCost,Tax,Total,OrderDate");

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

            //Write all orders
            for(Order order : orders.values()){
                writer.println(
                        order.getOrderNumber() + DELIMITER
                                + order.getCustomerName() + DELIMITER
                                + order.getState() + DELIMITER
                                + order.getTaxRate() + DELIMITER
                                + order.getProductType() + DELIMITER
                                + order.getArea() + DELIMITER
                                + order.getCostPerSquareFoot() + DELIMITER
                                + order.getLaborCostPerSquareFoot() + DELIMITER
                                + order.getMaterialCost() + DELIMITER
                                + order.getLaborCost() + DELIMITER
                                + order.getTax() + DELIMITER
                                + order.getTotal() + DELIMITER
                                + dateFormat.format(order.getOrderDate())

                );
            }
        }catch (IOException e){
            throw new PersistenceException("Could not export order data." , e);
        }

    }


    /**
     * Exports all order data
     * @param orders
     * @throws PersistenceException
     */
    @Override
    public void exportData(Map<Integer, Order> orders) throws PersistenceException {
      writeToFile(orders);
    }
}
