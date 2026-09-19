package com.mthree.service;

import com.mthree.exceptions.NoSuchOrderException;
import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;
import com.mthree.model.Product;
import com.mthree.model.Tax;

import java.time.LocalDate;
import java.util.List;

public interface ServiceLayer {

    public int getNextOrderNumber() throws PersistenceException;
    public Order calculateOrder(Order order) throws PersistenceException;
    public Order addOrder(Order order) throws PersistenceException;
    public Order getOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException;
    public Order editOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException;
    public List<Order> getOrdersForDate(LocalDate date) throws  PersistenceException;
    public Order removeOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException;
    public void exportData();
    public List<Tax> getTaxes();
    public List<Product>  getProducts();


}
