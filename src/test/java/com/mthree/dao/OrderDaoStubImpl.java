package com.mthree.dao;

import com.mthree.exceptions.NoSuchOrderException;
import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;

import java.util.*;

public class OrderDaoStubImpl  implements OrderDao {

    private Order testOrder;

    /**
     * Creates test data for the stub.
     */
    public OrderDaoStubImpl() {

        testOrder = new Order();
        testOrder.setOrderNumber(1);
        testOrder.setCustomerName("Test Customer");
    }

    /**
     * Returns a fixed order number for testing.
     */
    @Override
    public int getNextOrderNumber() throws PersistenceException {
        return 2;
    }

    /**
     * Returns the order passed to the method.
     */
    @Override
    public Order addOrder(Order order) throws PersistenceException {
        return order;
    }

    /**
     * Returns the test order when order number 1 is requested.
     */
    @Override
    public Order getOrder(Date date, int orderNumber)
            throws PersistenceException, NoSuchOrderException {

        if (orderNumber == testOrder.getOrderNumber()) {
            return testOrder;
        }

        throw new NoSuchOrderException("Order not found.");
    }

    /**
     * Returns the test order for editing.
     */
    @Override
    public Order editOrder(Date date, int orderNumber)
            throws PersistenceException, NoSuchOrderException {

        if (orderNumber == testOrder.getOrderNumber()) {
            return testOrder;
        }

        throw new NoSuchOrderException("Order not found.");
    }

    /**
     * Returns a list containing the test order.
     */
    @Override
    public List<Order> getOrdersForDate(Date date)
            throws PersistenceException {

        List<Order> orders = new ArrayList<>();
        orders.add(testOrder);

        return orders;
    }

    /**
     * Returns a map containing the test order.
     */
    @Override
    public Map<Integer, Order> getAllOrders()
            throws PersistenceException {

        Map<Integer, Order> orders = new HashMap<>();
        orders.put(testOrder.getOrderNumber(), testOrder);

        return orders;
    }

    /**
     * Returns the test order when it is removed.
     */
    @Override
    public Order removeOrder(Date date, int orderNumber)
            throws PersistenceException, NoSuchOrderException {

        if (orderNumber == testOrder.getOrderNumber()) {
            return testOrder;
        }

        throw new NoSuchOrderException("Order not found.");
    }
}
