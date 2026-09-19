package com.mthree.dao;

import com.mthree.exceptions.NoSuchOrderException;
import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class OrderDaoFileImpl implements OrderDao{

    private static String DELIMITER = ",";
    private static  String ORDER_FOLDER = "Orders/";
    private Map <Integer, Order> orders = new HashMap<>();
    private int largestOrderNumber;


    /**
     * Groups orders by date before writing them to their corresponding files
     */
    private void writeToFile() throws PersistenceException {

        //Store orders grouped by sales date
        Map<String, List<Order>> ordersByDate = new HashMap<>();

        //Format dates to match the required order file name
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");

        //Go through all orders currently stored in memory
        for(Order order : orders.values()){

            //Convert the order date to MMddyyyy format
            String dateString = dateFormat.format(order.getOrderDate());

            //Create a list for the date if needed and add the order to it
            ordersByDate
                    .computeIfAbsent(dateString, key -> new ArrayList<>())
                    .add(order);
        }

        for (Map.Entry<String , List<Order>> entry : ordersByDate.entrySet()){

            //Get the date and the orders belonging to that date
            String dateString = entry.getKey();
            List<Order> ordersForDate = entry.getValue();

            //Build the required order file name
            String fileName = ORDER_FOLDER + "Orders_" + dateString + ".txt";

            try(PrintWriter writer = new PrintWriter(fileName)){

                //Write the header row
                writer.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,"
                        + "CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,"
                        + "LaborCost,Tax,Total");

                //Write each order for the current date
                for(Order order : ordersForDate){
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
                            + order.getTotal()

                    );
                }
            }catch (FileNotFoundException e){
                throw new PersistenceException("Order file could not be written", e );
            }
        }


    }

    /**
     * Load all orders from the order files into memory
     */
    private void loadFromFile() throws PersistenceException{
        //Clear existing orders before loading the files
        orders.clear();
        largestOrderNumber = 0;

        //Open the order folder
        File folder = new File(ORDER_FOLDER);
        File[] orderFiles = folder.listFiles();

        //Check that the Orders folder exists
        if(orderFiles == null){
            throw new PersistenceException("Orders folder could not be found");
        }

        // Read each Orders file
        for (File orderFile : orderFiles){
            //Ignore everything that is not an Order file
            if(!orderFile.isFile() || !orderFile.getName().startsWith("Orders_")){
                continue;
            }

            //Get the file name
            String fileName = orderFile.getName();
            //Extract the data from the file name
            String dateString = fileName
                    .replace("Orders_" , "")
                    .replace(".txt", "");

            Date orderDate ;

            try{
                //Convert the date String into a Date object
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
                orderDate = dateFormat.parse(dateString);
            }catch(ParseException e){
                throw new PersistenceException("Invalid date in order file name", e);
            }


            try (Scanner scanner = new Scanner(orderFile)){
                //Skip the header row
                if(scanner.hasNextLine()){
                    scanner.nextLine();
                }

                //Read each order from the file
                while (scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    String[] values = line.split(DELIMITER);

                    //Find how many commas are parte of the customer name
                    int customerNameParts = values.length -11;

                    //Build the complete customer name
                    StringBuilder customerName = new StringBuilder(values[1]);

                    for (int i =2; i <= customerNameParts; i++){
                        customerName.append(",").append(values[i]);
                    }

                    //Position of the state after the customer name
                    int stateIndex = customerNameParts + 1;

                    //Create the Order object
                    Order order = new Order();

                    //Set the order date from the file name
                    order.setOrderDate(orderDate);

                    //Set the order data from teh current row
                    order.setOrderNumber(Integer.parseInt(values[0]));
                    order.setCustomerName(customerName.toString());
                    order.setState(values[stateIndex]);
                    order.setTaxRate(new BigDecimal(values[stateIndex + 1]));
                    order.setProductType(values[stateIndex + 2]);
                    order.setArea(new BigDecimal(values[stateIndex + 3]));
                    order.setCostPerSquareFoot(new BigDecimal(values[stateIndex + 4]));
                    order.setLaborCostPerSquareFoot(new BigDecimal(values[stateIndex + 5]));
                    order.setMaterialCost(new BigDecimal(values[stateIndex +6]));
                    order.setLaborCost(new BigDecimal(values[stateIndex +7]));
                    order.setTax(new BigDecimal(values[stateIndex +8]));
                    order.setTotal(new BigDecimal(values[stateIndex +9]));

                    //Store the order using its order number as the key
                    orders.put(order.getOrderNumber(), order);

                    //Keep track of the largest existing order number
                    if (order.getOrderNumber() > largestOrderNumber){
                        largestOrderNumber = order.getOrderNumber();
                    }

                }
            }catch(FileNotFoundException e){
                throw new PersistenceException("Order file could not be found" , e );
            }
        }


    }

    /**
     *
     * @return the next available order number
     */
    @Override
    public int getNextOrderNumber() throws PersistenceException {
        loadFromFile();
        return largestOrderNumber + 1;
    }

    /**
     * Adds a new order and saves the updated orders to file
     * @param order
     * @return
     */
    @Override
    public Order addOrder(Order order) throws PersistenceException{
         //Load all existing orders
        loadFromFile();

        //Add new order to the map
        orders.put(order.getOrderNumber(), order);

        //Save the updated orders to the order files
        writeToFile();

        //Return the added order
        return order;
    }


    /**
     * Returns an order matching the given date and order number
     * @param date the order date
     * @param orderNumber the order number
     * @return the matching order
     * @throws PersistenceException if the order files cannot be read
     * @throws NoSuchOrderException ift the order does not exist
     */
    @Override
    public Order getOrder(Date date, int orderNumber) throws PersistenceException , NoSuchOrderException {
        //Load all existing files
        loadFromFile();

        //Find the order by its order number
        Order order = orders.get(orderNumber);

        //Check that the order exists
        if(order == null){
            throw new NoSuchOrderException(
                    "Order " + orderNumber + " could not be found"
            );
        }

        //Check that the order belongs to the requested date
        SimpleDateFormat dateFormat = new SimpleDateFormat(("MMddyyyy"));

        String requestedDate = dateFormat.format(date);
        String actualDate = dateFormat.format(order.getOrderDate());

        if ( !requestedDate.equals(actualDate)){
            throw new NoSuchOrderException(
                    "Order " + orderNumber + " could not be found for this date"
            );
        }
        return order;
    }


    /**
     * Saves changes made to an existing order
     * @param date the order date
     * @param orderNumber the order number
     * @return the updated order
     * @throws PersistenceException if the order cannot be saved
     * @throws NoSuchOrderException if the order does not exist
     */
    @Override
    public Order editOrder(Date date, int orderNumber)  throws PersistenceException, NoSuchOrderException {

        //Find teh order currently stored in memory
        Order order = orders.get(orderNumber);

        //Check that the order exists
        if(order == null ){
            throw new NoSuchOrderException(
                    "Order " + orderNumber + " could not be found"
            );
        }
        //Check that the order belongs to the requested date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");

        String requestedDate = dateFormat.format(date);
        String actualDate = dateFormat.format(order.getOrderDate());

        if(!requestedDate.equals(actualDate)){
            throw new NoSuchOrderException(
                    "Order " + orderNumber + " could not be found for this date");

        }
        //Save the updated order to file
        writeToFile();

        return order;
    }


    /**
     * Returns all orders from the given date
     * @param date the order date
     * @return a list of orders for the given date
     * @throws PersistenceException if the order files cannot be read
     */
    @Override
    public List<Order> getOrdersForDate(Date date) throws  PersistenceException{
        //Load all existing orders
        loadFromFile();

        //Store orders matching the requested date
        List<Order> ordersForDate = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
        String requestedDate = dateFormat.format(date);

        //Check each order
        for(Order order : orders.values()){

            String orderDate = dateFormat.format(order.getOrderDate());

            if(requestedDate.equals(orderDate)){
                ordersForDate.add(order);
            }
        }
        return ordersForDate;
    }


    /**
     * Returns all existing orders
     * @return a map containing all orders
     * @throws PersistenceException if the order files cannot be read
     */
    @Override
    public Map<Integer, Order> getAllOrders() throws PersistenceException{

        //Load all existing orders
        loadFromFile();

        //Return all orders stored in memory
        return new HashMap<>(orders);
    }

    /**
     * Removes an existing order and saves the changes to file
     * @param date the order date
     * @param orderNumber the order number
     * @return the removed order
     * @throws PersistenceException if the order cannot be saved
     * @throws NoSuchOrderException if the order does not exists
     */
    @Override
    public Order removeOrder(Date date, int orderNumber) throws  PersistenceException, NoSuchOrderException {

        //Load all existing orders
        loadFromFile();

        //Find the order by its order number
        Order order = orders.get(orderNumber);

        //Check that order exists
        if (order == null) {
                        throw new NoSuchOrderException(
                                "Order " + orderNumber + " could not be found"
                        );
        }


        //Check that the order belongs to the requested date
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");

        String requestedDate = dateFormat.format(date);
        String actualDate = dateFormat.format(order.getOrderDate());

        if(!requestedDate.equals(actualDate)) {
            throw new NoSuchOrderException(
                    "Order " + orderNumber + " could not be found for this date");

        }

        //Remove the order from memory
        orders.remove(orderNumber);

        //Check if any orders remain for this date
        boolean orderRemainForDate =  false;

        for(Order remainingOrder : orders.values()){
            String remainingDate = dateFormat.format(remainingOrder.getOrderDate());

            if (requestedDate.equals((remainingDate))){
                orderRemainForDate = true;
                break;
            }
        }

        //If no orders remain, delete the empty order file
        if(!orderRemainForDate){

            String fileName = ORDER_FOLDER + "Orders_" + requestedDate + ".txt";

            File orderFile = new File(fileName);

            if(orderFile.exists() && !orderFile.delete()){
                throw  new PersistenceException(
                        "Order file could not be deleted"
                );
            }
        }

        //Save the changes  and remaining orders to file
        writeToFile();

        //Return the removed order
        return order;
    }
}
