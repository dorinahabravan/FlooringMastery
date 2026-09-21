package com.mthree.dao;

import com.mthree.exceptions.NoSuchOrderException;
import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class OrderDaoFileImplTest {

        private OrderDao orderDao;
        private Date testDate;

        /**
         * Sets up the DAO and test date before each test
         */

        @BeforeEach
        public void setUP() throws PersistenceException , ParseException{

            //Create DAO
            orderDao = new OrderDaoFileImpl();

            //Create a test date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");

            testDate = dateFormat.parse("12312026");

            //Remove old test data if it exists
            try{

                orderDao.removeOrder(testDate, 999);
            } catch (NoSuchOrderException e) {
                //No older test order exists
            }
        }

        /**
         * Removes test data after each test
         */
        @AfterEach
        public void tearDown() throws  PersistenceException{

            //Clean up the test order
            try{
                orderDao.removeOrder(testDate, 999);

            }catch (NoSuchOrderException e){
                //Test order has already been removed
            }
        }


        /**
         * Creates a standard order used by the tests
         */
        private Order createTestOrder(){

            //Create test order
            Order order = new Order();

            order.setOrderNumber(999);
            order.setOrderDate(testDate);
            order.setCustomerName("Test Customer");
            order.setState("TX");
            order.setTaxRate(new BigDecimal("4.45"));
            order.setProductType("Tile");
            order.setArea(new BigDecimal("200"));
            order.setCostPerSquareFoot(new BigDecimal("3.50"));
            order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
            order.setMaterialCost(new BigDecimal("700.00"));
            order.setLaborCost(new BigDecimal("830.00"));
            order.setTax(new BigDecimal("68.09"));
            order.setTotal(new BigDecimal("1598.09"));

            return order;

        }


        /**
         * Test adding and retrieving an order
         */
        @Test
         public void testAddAndGetOrder()
                throws PersistenceException, NoSuchOrderException {

            // ARRANGE
            Order order = createTestOrder();

            //ACT
            orderDao.addOrder(order);

            Order retrieveOrder = orderDao.getOrder(testDate, 999);

            //ASSERT
            assertNotNull(retrieveOrder, "Retrieved order should not be null.");
            assertEquals(999, retrieveOrder.getOrderNumber(), "Order number should match.");
            assertEquals("Test Customer", retrieveOrder.getCustomerName(), "Customer name should match.");
            assertEquals("TX", retrieveOrder.getState(), "State should match");
            assertEquals("Tile", retrieveOrder.getProductType(), "Product type should match.");
            assertEquals(new BigDecimal("200"), retrieveOrder.getArea(), "Area should match");

        }


    /**
     * Test retrieving all orders for a date
     */
    @Test
    public void testGetOrdersForDate() throws PersistenceException{

        // ARRANGE
        Order order = createTestOrder();
        orderDao.addOrder(order);

        //ACT
        List<Order> orders = orderDao.getOrdersForDate(testDate);

        //ASSERT
        assertNotNull(orders, "Orders list should not be null");
        assertFalse(orders.isEmpty(), "Orders list should should not be empty");
        assertTrue(orders.stream()
                .anyMatch(o -> o.getOrderNumber() == 999),
                "Order 999 should be found for the test date.");

    }

        /**
         * Test retrieving all existing orders
         */

        @Test
        public void testGetAllOrders() throws PersistenceException{

            // ARRANGE
            Order order = createTestOrder();
            orderDao.addOrder(order);

            //ACT
            Map<Integer , Order> orders = orderDao.getAllOrders();

            //ASSERT
            assertNotNull(orders, "Orders map should not be null");
            assertTrue(orders.containsKey(999), "Orders should contain order 999");
            assertEquals("Test Customer", orders.get(999).getCustomerName(), "Customer name should match");


        }


    /**
     * Test editing an existing order
     */
    @Test
    public void testEditOrder() throws PersistenceException , NoSuchOrderException {

        // ARRANGE
        Order order = createTestOrder();
        orderDao.addOrder(order);

        Order orderToEdit = orderDao.getOrder(testDate, 999);

        orderToEdit.setCustomerName("Edited Customer");
        orderToEdit.setArea(new BigDecimal("300"));

        //ACT
        orderDao.editOrder(testDate, 999);
        Order editedOrder = orderDao.getOrder(testDate, 999);

        //ASSERT
        assertEquals("Edited Customer", editedOrder.getCustomerName(), "Customer name should be updated");
        assertEquals(new BigDecimal("300"), editedOrder.getArea(),
                "Area should be updated");

    }



    /**
     * Test removing an existing order
     */
    @Test
    public void testRemoveOrder() throws PersistenceException , NoSuchOrderException {

        // ARRANGE
        Order order = createTestOrder();
        orderDao.addOrder(order);

        //ACT
        Order removedOrder = orderDao.removeOrder(testDate, 999);

        //ASSERT
        assertNotNull(removedOrder, "Removed order should not be null");
        assertEquals(999, removedOrder.getOrderNumber(), "Removed order number should be 999");
        assertThrows(NoSuchOrderException.class, () -> orderDao.getOrder(testDate, 999),
                "Removed order number should be 999");

    }

    /**
     * Test requesting an order that does not exist
     */

    @Test
    public  void testGetOrderNotFound() throws PersistenceException{

        //ACT and ASSERT
        assertThrows(NoSuchOrderException.class, () -> orderDao.getOrder(testDate, 999),
        "A missing order should throw NoSuchOrderException");
    }

    /**
     * Test generation of the next order number
     */

    @Test
    public  void testGetNextOrderNumber() throws PersistenceException{
        //ACT
        int nextOrderNumber = orderDao.getNextOrderNumber();

        //ASSERT
        assertTrue(nextOrderNumber > 0, "Next order number should be greater than zero");
    }



    }
