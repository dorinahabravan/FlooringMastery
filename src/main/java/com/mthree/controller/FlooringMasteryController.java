package com.mthree.controller;

import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;
import com.mthree.service.ServiceLayer;

import com.mthree.view.FlooringMasteryView;

import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryController {
    private FlooringMasteryView view;
    private ServiceLayer service;


    /**
     * Creates the Controller with the required dependencies
     *
     */
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
                    uknownCommand();
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

    private void addOrder(){

    }

    private void editOrder(){

    }
    private void removeOrder(){

    }

    private void exportData(){

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
    private void uknownCommand(){
        view.displayUnknownCommandMessage();

    }



}
