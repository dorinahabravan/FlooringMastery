package com.mthree.service;

import com.mthree.dao.ExportDao;
import com.mthree.dao.OrderDao;
import com.mthree.dao.ProductDao;
import com.mthree.dao.TaxDao;
import com.mthree.exceptions.NoSuchOrderException;
import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;
import com.mthree.model.Product;
import com.mthree.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ServiceLayerImpl implements ServiceLayer {

    private OrderDao orderDao;
    private TaxDao taxDao;
    private ProductDao productDao;
    private ExportDao exportDao;


    /**
     * Creates the Service Layer with the required DAO dependencies
     * @param orderDao
     * @param taxDao
     * @param productDao
     */
    @Autowired
    public ServiceLayerImpl(OrderDao orderDao, TaxDao taxDao, ProductDao productDao, ExportDao exportDao){
        this.orderDao = orderDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
        this.exportDao = exportDao;
    }

    /**
     * Converts a LocalDate to Date for use by the DAO Layer
     * @param localDate the date to convert
     * @return the converted date
     */
    private Date convertToDate(LocalDate localDate){
        return java.sql.Date.valueOf(localDate);

    }


    /**
     * Gets the next available order nummber
     * @return the next order number
     * @throws PersistenceException if order data cannot be accessed
     */
    @Override
    public int getNextOrderNumber() throws PersistenceException {
        return orderDao.getNextOrderNumber();
    }

    /**
     * Applies tax and product information and calculates all costs for an order
     * @param order the order to calculate
     * @return the calculated order
     * @throws PersistenceException
     */
    @Override
    public Order calculateOrder(Order order) throws PersistenceException {
        //Find and apply the tax rate
        for(Tax tax : taxDao.getAllTaxes()){
            if(tax.getStateAbr().equalsIgnoreCase(order.getState())){
                order.setTaxRate(tax.getTaxRate());
                break;
            }
        }

        //Find and apply the product costs
        for(Product product : productDao.getAllProducts()){
            if(product.getProductType().equalsIgnoreCase(order.getProductType())){
                order.setCostPerSquareFoot(product.getCostPerSquareFoot());

                order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
                break;
            }
        }

        //Generate the next order number for a new order
        if(order.getOrderNumber() == 0){
            order.setOrderNumber(orderDao.getNextOrderNumber());
        }

        //Calculate all order costs
        calculateOrderCosts(order);
        return order;
    }


    /**
     * Adds a confirmed order to storage
     * @param order  to add
     * @return the added order
     * @throws PersistenceException if order data cannot be saved
     */
    @Override
    public Order addOrder(Order order) throws PersistenceException {

        //Generate an order number if one has not already been assigned
        if(order.getOrderNumber() == 0){
            order.setOrderNumber(orderDao.getNextOrderNumber());
        }

        // Save the completed order
        return orderDao.addOrder(order);
    }

    /**
     * Gets an order for a specific date and order number
     * @param date the order date
     * @param orderNumber the order number
     * @return the requested order
     * @throws PersistenceException if order cannot be accessed
     * @throws NoSuchOrderException if order cannot be found
     */
    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException {

        Date convertDate = convertToDate(date);
        return orderDao.getOrder(convertDate, orderNumber);
    }

    /**
     * Recalculates all costs for an order
     * @param order to recalculate
     */
    private void calculateOrderCosts(Order order){

        //Calculate the material cost
        BigDecimal materialCost =
                order.getArea().multiply(order.getCostPerSquareFoot())
                        .setScale(2, RoundingMode.HALF_UP);
                order.setMaterialCost(materialCost);

        //Calculate the labor cost
        BigDecimal laborCost =
                order.getArea().multiply(order.getLaborCostPerSquareFoot())
                               .setScale(2, RoundingMode.HALF_UP);;
        order.setLaborCost(laborCost);

        //Calculate the tax
        BigDecimal taxRate = order.getTaxRate().divide(new BigDecimal("100"));

        BigDecimal taxCalculation = materialCost.add(laborCost)
                                 .multiply(taxRate)
                                 .setScale(2, RoundingMode.HALF_UP);
        order.setTax(taxCalculation);

        //Calculate total cost
        BigDecimal total = materialCost.add(laborCost)
                                  .add(taxCalculation)
                                  .setScale(2, RoundingMode.HALF_UP);;
        order.setTotal(total);




    }

    /**
     * Saves an edited order for the specified date and order number
     * @param date
     * @param orderNumber
     * @return the edited order
     * @throws PersistenceException if order data cannot be accessed or saved
     * @throws NoSuchOrderException if the order cannot be found
     */
    @Override
    public Order editOrder(LocalDate date, int orderNumber)  throws PersistenceException, NoSuchOrderException{

        //Convert the date before passing it to the DAO
        Date convertDate = convertToDate(date);

        //Save the edited order
        return orderDao.editOrder(convertDate, orderNumber);
    }


    /**
     * Gets all  orders for a specific date
     * @param date
     * @return a list of orders for the selected date
     * @throws PersistenceException if order data cannot be accessed
     */
    @Override
    public List<Order> getOrdersForDate(LocalDate date)  throws  PersistenceException{

        Date convertedDate = convertToDate(date);
        return orderDao.getOrdersForDate(convertedDate);
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber)  throws PersistenceException, NoSuchOrderException{

        //Convert the date before passing it to the DAO
        Date convertedDate = convertToDate(date);

        //Remove the order and save the changes
        return orderDao.removeOrder(convertedDate, orderNumber);
    }


    /**
     * Exports all current orders
     */
    @Override
    public void exportData() throws PersistenceException{

        //Get al current orders
        Map<Integer, Order > orders = orderDao.getAllOrders();

        //Export the orders to the backup files
        exportDao.exportData(orders);

    }


    /**
     * Gets all available tax information
     * @return a lsit of all taxes
     */
    @Override
    public List<Tax> getTaxes() {

        //Retrieve all tax information from the Tax DAO
        return taxDao.getAllTaxes();
    }

    /**
     * Gets all available products
     * @return a list of all products
     */
    @Override
    public List<Product> getProducts() {

        //Retrieve all product information from the Product DAO
        return productDao.getAllProducts();
    }
}
