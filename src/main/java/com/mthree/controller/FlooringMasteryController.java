package com.mthree.controller;

import com.mthree.exceptions.NoSuchOrderException;
import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;
import com.mthree.service.ServiceLayer;

import com.mthree.view.FlooringMasteryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FlooringMasteryController {
    private FlooringMasteryView view;
    private ServiceLayer service;


    /**
     * Creates the Controller with the required dependencies
     *
     */
    @Autowired
    public FlooringMasteryController(FlooringMasteryView view , ServiceLayer service){
        this.view = view;
        this.service = service;
    }

    /**
     * Runs the Flooring Mastery application
     */
    public void run(){

        boolean keepGoing = true ;

        while (keepGoing){
            int menuSelection = getMenuSelection();

            switch(menuSelection){
                case 1:
                    displayOrders();
                    break;
                case 2:
                    addOrder();
                    break;
                case 3:
                    editOrder();
                    break;
                case 4:
                    removeOrder();
                    break;
                case 5:
                    exportData();
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
        }
        exitMessage();

    }

    /**
     * Gets the user's main menu selection
     * @return the selected menu option
     */
    private int  getMenuSelection(){
        return view.displayMainMenuAndGetSelection();
    }

    /**
     * Displays all orders for a selected date
     */
    private void displayOrders(){

        //Get the date from the user
        LocalDate date = view.getDateInput();

        try{
            //Get all orders for the selected date
            List<Order> orders = service.getOrdersForDate(date);

            //Check if any orders exist for the date
            if(orders.isEmpty()) {
                view.displayErrorMessage("No orders found for this date.");
            }else {
                view.displayOrders(orders);
            }
        }catch (PersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        }

    }


    /**
     * Handles adding a new order
     */
    private void addOrder(){
        view.displayAddOrderBanner();

        try{
            //Get the order information from the user
            Order order = view.getAddOrderInput(
                    service.getTaxes(),
                    service.getProducts());
            //Calculate all order costs
            order = service.calculateOrder(order);

            //Display the order summary
            view.displayOrderInfo(order);

            //Ask the user to confirm the order
            if(view.getConfirmation()){
                service.addOrder(order);
                view.displayAddOrderSuccess();

            }


        }catch(PersistenceException e){
            view.displayErrorMessage(e.getMessage());
        }

    }


    /**
     * Handles editing an existing orde
     */
    private void editOrder() {
        view.displayEditOrderBanner();

        boolean validOrder = false;

        while (!validOrder) {

            //Get the date and order number
            LocalDate date = view.getDateInput();

            try {

                //Check if there are any orders for this date
                List<Order> orders = service.getOrdersForDate(date);

                if(orders.isEmpty()){
                    view.displayErrorMessage("No orders found for this date.");
                    continue;
                }

                //Get the order number
                int orderNumber = view.getOrderNumberInput();

                //Find the existing order
                Order order = service.getOrder(date, orderNumber);

                validOrder = true;

                //Get the updated information

                order = view.getEditOrderInput(
                        order, service.getTaxes(), service.getProducts());

                //Recalculate costs after any changes
                order = service.calculateOrder(order);

                //Display teh updated order
                view.displayOrderInfo(order);

                //Save the changes only if confirmed
                if (view.getConfirmation()) {
                    service.editOrder(date, orderNumber);
                    view.displayEditOrderSuccess();
                }

            } catch (NoSuchOrderException e) {
                view.displayErrorMessage(e.getMessage());
            } catch (PersistenceException e) {
                view.displayErrorMessage(e.getMessage());
                return;
            }
        }

    }

    /**
     * Handles removing an existing order
     */
    private void removeOrder(){
        view.displayRemoveOrderBanner();

            //Get the date
            LocalDate date = view.getDateInput();

            try{

                    //Check if there are any orders for this date
                    List<Order> orders = service.getOrdersForDate(date);

                    if(orders.isEmpty()){
                        view.displayErrorMessage("No orders found for this date.");
                        return;
                    }

                    //Only ask the order number if order exists for this date
                    int orderNumber = view.getOrderNumberInput();

                    //Find the existing orde
                Order order = service.getOrder(date, orderNumber);

                //Display the order before removal
                view.displayOrderInfo(order);

                //Ask the user to confirm the removal
                if(view.getConfirmation()){
                    service.removeOrder(date,orderNumber);
                    view.displayRemoveOrderSuccess();

                }
            }catch (NoSuchOrderException e){
                view.displayErrorMessage(e.getMessage());

            }catch(PersistenceException e){
                view.displayErrorMessage(e.getMessage());

            }


    }

    /**
     * Handles exporting all order data
     */
    private void exportData(){

        try{

            //Export all data
            service.exportData();

            //Display success message
            view.displayExportDataSuccess();
        }catch (PersistenceException e){
            view.displayErrorMessage(e.getMessage());
        }

    }

    /**
     * Displays the application exit message
     */
    private void exitMessage(){
        view.displayExitMessage();

    }

    /**
     * Display a message for an unknown command
     */
    private void unknownCommand(){
        view.displayUnknownCommandMessage();

    }



}
