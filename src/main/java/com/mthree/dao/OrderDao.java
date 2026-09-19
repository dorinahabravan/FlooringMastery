package com.mthree.dao;

import com.mthree.exceptions.NoSuchOrderException;
import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {


   public int getNextOrderNumber() throws PersistenceException;
   public Order addOrder( Order order) throws PersistenceException;
   public Order getOrder(Date date, int orderNumber) throws PersistenceException, NoSuchOrderException;
   public Order editOrder(Date date , int orderNumber) throws PersistenceException, NoSuchOrderException;
   public List<Order> getOrdersForDate(Date date) throws PersistenceException;
   public Map<Integer, Order>  getAllOrders() throws PersistenceException;
   public Order removeOrder(Date date , int orderNumber) throws PersistenceException, NoSuchOrderException;





}
