package com.mthree.service;

import com.mthree.dao.*;
import com.mthree.model.Order;
import com.mthree.model.Product;
import com.mthree.model.Tax;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceLayerImplTest {

    private ServiceLayer service;

    /**
     * Creates the Service Layer with stub DAOs before each test
     */

    @BeforeEach
    public void setUp() {
        //Create stub dependencies
        OrderDao orderDao = new OrderDaoStubImpl();
        TaxDao taxDao = new TaxDaoStubImpl();
        ProductDao productDao = new ProductDaoStubImpl();
        ExportDao exportDao = new ExportDaoStubImpl();

        //Inject the stub DAOs into the real Service Layer
        service = new ServiceLayerImpl(
                orderDao, taxDao, productDao, exportDao);
    }

    /**
     * Cleans up after each test
     */

    @AfterEach

    public void tearDown() {
        service = null;
    }


    /**
     * Tests the calculation of an order.
     */
    @Test
    public void testCalculateOrder() throws Exception {

        // ARRANGE
        Order order = new Order();
        order.setCustomerName("Test Customer");
        order.setState("TX");
        order.setProductType("Tile");
        order.setArea(new BigDecimal("200"));

        // ACT
        Order calculatedOrder = service.calculateOrder(order);

        // ASSERT
        assertNotNull(calculatedOrder,
                "Calculated order should not be null.");

        assertEquals(2,
                calculatedOrder.getOrderNumber(),
                "Order number should be 2.");

        assertEquals(new BigDecimal("4.45"),
                calculatedOrder.getTaxRate(),
                "Tax rate should be 4.45.");

        assertEquals(new BigDecimal("3.50"),
                calculatedOrder.getCostPerSquareFoot(),
                "Cost per square foot should be 3.50.");

        assertEquals(new BigDecimal("4.15"),
                calculatedOrder.getLaborCostPerSquareFoot(),
                "Labor cost per square foot should be 4.15.");

        assertEquals(new BigDecimal("700.00"),
                calculatedOrder.getMaterialCost(),
                "Material cost should be 700.00.");

        assertEquals(new BigDecimal("830.00"),
                calculatedOrder.getLaborCost(),
                "Labor cost should be 830.00.");

        assertEquals(new BigDecimal("68.09"),
                calculatedOrder.getTax(),
                "Tax should be 68.09.");

        assertEquals(new BigDecimal("1598.09"),
                calculatedOrder.getTotal(),
                "Total should be 1598.09.");
    }


    /**
     * Tests getting the next available order number.
     */
    @Test
    public void testGetNextOrderNumber() throws Exception {

        // ACT
        int orderNumber = service.getNextOrderNumber();

        // ASSERT
        assertEquals(2, orderNumber);
    }


    /**
     * Tests adding a new order.
     */
    @Test
    public void testAddOrder() throws Exception {

        // ARRANGE
        Order order = new Order();
        order.setCustomerName("Test Customer");

        // ACT
        Order addedOrder = service.addOrder(order);

        // ASSERT
        assertNotNull(addedOrder);
        assertEquals(2, addedOrder.getOrderNumber());
        assertEquals("Test Customer", addedOrder.getCustomerName());
    }


    /**
     * Tests getting an existing order.
     */
    @Test
    public void testGetOrder() throws Exception {

        // ARRANGE
        LocalDate date = LocalDate.of(2026, 12, 31);

        // ACT
        Order order = service.getOrder(date, 1);

        // ASSERT
        assertNotNull(order);
        assertEquals(1, order.getOrderNumber());
        assertEquals("Test Customer", order.getCustomerName());
    }


    /**
     * Tests getting orders for a specific date.
     */
    @Test
    public void testGetOrdersForDate() throws Exception {

        // ARRANGE
        LocalDate date = LocalDate.of(2026, 12, 31);

        // ACT
        List<Order> orders = service.getOrdersForDate(date);

        // ASSERT
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(1, orders.get(0).getOrderNumber());
    }


    /**
     * Tests editing an existing order.
     */
    @Test
    public void testEditOrder() throws Exception {

        // ARRANGE
        LocalDate date = LocalDate.of(2026, 12, 31);

        // ACT
        Order editedOrder = service.editOrder(date, 1);

        // ASSERT
        assertNotNull(editedOrder);
        assertEquals(1, editedOrder.getOrderNumber());
    }


    /**
     * Tests removing an existing order.
     */
    @Test
    public void testRemoveOrder() throws Exception {

        // ARRANGE
        LocalDate date = LocalDate.of(2026, 12, 31);

        // ACT
        Order removedOrder = service.removeOrder(date, 1);

        // ASSERT
        assertNotNull(removedOrder);
        assertEquals(1, removedOrder.getOrderNumber());
    }


    /**
     * Tests getting all tax information.
     */
    @Test
    public void testGetTaxes() {

        // ACT
        List<Tax> taxes = service.getTaxes();

        // ASSERT
        assertNotNull(taxes);
        assertEquals(1, taxes.size());
        assertEquals("TX", taxes.get(0).getStateAbr());
        assertEquals(new BigDecimal("4.45"),
                taxes.get(0).getTaxRate());
    }


    /**
     * Tests getting all product information.
     */
    @Test
    public void testGetProducts() {

        // ACT
        List<Product> products = service.getProducts();

        // ASSERT
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Tile", products.get(0).getProductType());
        assertEquals(new BigDecimal("3.50"),
                products.get(0).getCostPerSquareFoot());
        assertEquals(new BigDecimal("4.15"),
                products.get(0).getLaborCostPerSquareFoot());
    }
    }


