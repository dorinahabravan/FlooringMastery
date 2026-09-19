package com.mthree.view;

import com.mthree.model.Order;
import com.mthree.model.Product;
import com.mthree.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@Component
public class FlooringMasteryView {

    private UserIO inputOutput;

    /**
     * Creates the View with the required UserIO dependency
     * @return inputOutput the userIO implementation
     */
    @Autowired
    public FlooringMasteryView(UserIO inputOutput){
        this.inputOutput = inputOutput;
    }

    /**
     * Displays the main menu and gets the user's selection
     * @return the selected menu option
     */
    public int displayMainMenuAndGetSelection (){

        inputOutput.print(
                "* * * * * * * * * * * * * * * * * * * * * * * * * * ");
        inputOutput.print("* <<Flooring Program>>");
        inputOutput.print("* 1. Display Orders");
        inputOutput.print("* 2. Add an Order");
        inputOutput.print("* 3. Edit an Order");
        inputOutput.print("* 4. Remove an Order");
        inputOutput.print("* 5. Export All Data");
        inputOutput.print("* 6. Quit");
        inputOutput.print("*");
        inputOutput.print(
                "* * * * * * * * * * * * * * * * * * * * * * * * * * ");

        return inputOutput.readInt("Please select an option:" , 1 ,6);
    }

    /**
     * Prompt the user until  a valid date is entered
     * @return the entered date
     */
    public LocalDate getDateInput(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        while(true){

            String dateInput = inputOutput.readString("Enter date (MM-dd-yyyy):");

            try{

                return LocalDate.parse(dateInput, formatter);
            }catch (DateTimeParseException e){
                inputOutput.print("Invalid date. Please use MM-dd-yyyy");
            }
        }

    }

    /**
     * Display all orders in the provided list
     * @param orders to display
     */
    public void displayOrders(List<Order> orders){

        inputOutput.print("=== Orders ===");

        //Display each order in the list
        for(Order order : orders){
            displayOrderInfo(order);
        }

    }

    /**
     * Displays the detail of a single order
     * @param order to display
     */
    public void displayOrderInfo(Order order){

        inputOutput.print("---------------------------------------");
        inputOutput.print("Order Number: " + order.getOrderNumber());
        inputOutput.print("Customer Name: " + order.getCustomerName());
        inputOutput.print("State: " + order.getState());
        inputOutput.print("Tax Rate: " + order.getTaxRate());
        inputOutput.print("Product Type: " + order.getProductType());
        inputOutput.print("Area: " + order.getArea());
        inputOutput.print("Cost Per Square Foot: " + order.getCostPerSquareFoot());
        inputOutput.print("Labor Cost Per Square Foot: " + order.getLaborCostPerSquareFoot());
        inputOutput.print("Material Cost: " + order.getMaterialCost());
        inputOutput.print("Labor Cost: " + order.getLaborCost());
        inputOutput.print("Tax: " + order.getTax());
        inputOutput.print("Total: " + order.getTotal());
        inputOutput.print("---------------------------------------");


    }

    /**
     * Displays the Add Order banner
     */
    public void displayAddOrderBanner(){
        inputOutput.print("=== Add Order ===");

    }

    /**
     * Gets the information required to create a new order
     * @param taxes available tax information
     * @param products the avaialable products
     * @return the new order
     */
    public Order getAddOrderInput (List<Tax> taxes, List<Product> products){

        Order order = new Order();

        //Get a future order date
        LocalDate orderDate;

        while(true){
            orderDate = getDateInput();

            if(orderDate.isAfter(LocalDate.now())){
                break;
            }
            inputOutput.print("Order date must be in the future");
        }

        order.setOrderDate(java.sql.Date.valueOf(orderDate));

        //Get and validate the customer name
        String customerInput = inputOutput.readString("Enter customer name:");

        String customerName = validateNameInput(customerInput);
        order.setCustomerName(customerName);

        //Get and validat the state
        String stateInput = inputOutput.readString("Enter state abbreviation:");

        Tax selectedTax = validateStateInput(stateInput, taxes);
        order.setState(selectedTax.getStateAbr());


        //Display all available products and pricing
        inputOutput.print("Available products:");
         for (Product product : products){
             inputOutput.print(
                     product.getProductType()
                     +" | Cost/sq ft: "
                     + product.getCostPerSquareFoot()
                     + " | Labor/sq ft: "
                     + product.getLaborCostPerSquareFoot());

         }


        //Get and validate the product
        String productInput = inputOutput.readString("Enter product type:");

        Product selectedProduct = validateProductInput(productInput, products);

        order.setProductType(selectedProduct.getProductType());

        //Get and validate teh area
        String areaInput = inputOutput.readString("Enter area:");

        BigDecimal area = validateAreaInput(areaInput);

        order.setArea(area);

        return order;
    }

    /**
     * Displays a message when an order is added successfully
     */
    public void displayAddOrderSuccess(){
        inputOutput.print("Order added successfully.");

    }

    /**
     * Displays the Edit Order banner
     */
    public void displayEditOrderBanner(){
        inputOutput.print("=== Edit Order ===");

    }

    /**
     * Gets yhe order number from the user
     * @return the entered order number
     */
    public int getOrderNumberInput(){

        return inputOutput.readInt("Enter order number:");
    }


    /**
     * Gets updated information for an existing order
     * Empty input keeps the current value
     * @param order the order to edit
     * @param taxes available tax information
     * @param products available products
     * @return the edited order
     */
    public Order getEditOrderInput(Order order, List<Tax> taxes, List<Product> products){

        //Get the new customer name or keep the current value
        String customerInput = inputOutput.readString("Enter customer name ("
                + order.getCustomerName()
                + ") [Press Enter to keep current]:");

        if(!customerInput.trim().isEmpty()){
            order.setCustomerName(validateNameInput(customerInput));
        }



        //Get the new state or keep the current  value
        String stateInput = inputOutput.readString("Enter state ("
                + order.getState()
                + ") [Press Enter to keep current]:");

        if(!stateInput.trim().isEmpty()){
            Tax selectedTax = validateStateInput(stateInput , taxes);

            order.setState(selectedTax.getStateAbr());
            order.setTaxRate(selectedTax.getTaxRate());
        }


        //Get new product or keep the current value
        String productInput = inputOutput.readString("Enter product type ("
                + order.getProductType()
                + ") [Press Enter to keep current]:");

        if(!productInput.trim().isEmpty()) {
            Product selectedProduct = validateProductInput(productInput, products);

            order.setProductType(selectedProduct.getProductType());

            order.setCostPerSquareFoot(selectedProduct.getCostPerSquareFoot());

            order.setLaborCostPerSquareFoot(selectedProduct.getLaborCostPerSquareFoot());

        }
            //Get the new area or keep the current value
            String areaInput = inputOutput.readString("Enter area ("
                    + order.getArea()
                    + ") [Press Enter to keep current]:");

            if (!areaInput.trim().isEmpty()) {
                order.setArea(validateAreaInput(areaInput));

            }

        return order;

    }


    /**
     * Display a message when an order is edited successfully
     */
    public void displayEditOrderSuccess(){
        inputOutput.print("Order edited successfully");

    }

    /**
     * Displays the Remove Order banner
     */
    public void displayRemoveOrderBanner(){
        inputOutput.print("=== Remove Order ===");

    }


    /**
     * Prompts the user for confirmation
     * @return true if confirmed, otherwise false
     */
    public boolean getConfirmation(){

        while(true){
            String confirmation =
                    inputOutput.readString("Would you like to continue?  (Y/N):");

            if(confirmation.equalsIgnoreCase("Y")){
                return true;
            }

            if (confirmation.equalsIgnoreCase("N")){
                return  false;
            }

            inputOutput.print("Invalid input. Please enter Y or N");
        }

    }

    /**
     * Displays a message whe an order is removed successfully
     */
    public void displayRemoveOrderSuccess(){
        inputOutput.print("Order removed successfully");

    }

    /**
     * Displays a message when all data is exported successfully
     */
    public void displayExportDataSuccess(){
        inputOutput.print("Data exported successfully");

    }


    /**
     * Displays the exit message
     */
    public void displayExitMessage(){
        inputOutput.print("Thank you for using the Flooring Mastery Program. Goodbye!");

    }

    /**
     * Displays an error message
     * @param errorMessage the error message to display
     */
    public void displayErrorMessage(String errorMessage){
        inputOutput.print("ERROR: " + errorMessage);


    }

    /**
     * Displays a message when an uknown command is entered
     *
     */
    public void displayUnknownCommandMessage(){
        inputOutput.print("Unknown command. Please try again.");

    }

    /**
     * Validates the customer name
     * The name may  contain letters, numbers, spaces, periods and commas
     * @param validateName the customer name to validate
     * @return a valid customer name
     */
    private String validateNameInput(String validateName){

        while (true){
        String name =  validateName.trim();

        if (!name.isEmpty() && name.matches("[a-zA-Z0-9., ]+")){
            return name;
        }

            inputOutput.print("Invalid name. Use only letters, numbers, spaces, periods and commas.");

            validateName = inputOutput.readString("Enter customer name:");

        }

    }

    /**
     * Validates that the entered state exists in the tax list
     * @param stateInput the state abbreviation to validate
     * @param taxes  available tax information
     * @return the matching Tax
     */
    private Tax validateStateInput(String stateInput, List<Tax> taxes){

        while(true){

            for (Tax tax : taxes){
                if(tax.getStateAbr().equalsIgnoreCase(stateInput.trim())){

                    return  tax;
                }
            }

            inputOutput.print("Invalid state.Please select an available state.");

            stateInput = inputOutput.readString("Enter state abbreviation");
        }

    }


    /**
     * Validates that the entered product exists in te product list
     * @param productInput the product type to validate
     * @param products the available products
     * @return the matching Product
     */
    private Product validateProductInput( String  productInput, List<Product> products){

        while(true){

            for(Product product : products){
                if(product.getProductType().equalsIgnoreCase(productInput.trim())){
                    return  product;
                }
            }

            inputOutput.print("Invalid product. Please select an available product");

            productInput = inputOutput.readString("Enter product type:");
        }

    }


    /**
     * Validates that entered area is at least 100 square feet
     * @param areaInput the area to validate
     * @return a valid area
     */
    private BigDecimal validateAreaInput(String areaInput){

        while (true){

            try{
                BigDecimal area = new BigDecimal((areaInput.trim()));

                if(area.compareTo(new BigDecimal("100")) >= 0){
                    return area;
                }

                inputOutput.print("Area must be at least 100 square feet");


            }catch(NumberFormatException e){
                inputOutput.print("Invalid area. Please enter a number.");
            }

            areaInput = inputOutput.readString("Enter area.");
        }

    }






}
