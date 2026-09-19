package com.mthree.dao;

import com.mthree.exceptions.NoSuchOrderException;
import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



    public class OrderDaoFileImplTest {

        @Test
        void testAddAndGetOrder()
                throws PersistenceException, NoSuchOrderException, ParseException {

            //Create DAO
            OrderDao dao = new OrderDaoFileImpl();

            //Create test date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
            Date date = dateFormat.parse("12312026");

            //Create test order
            Order order = new Order();
            order.setOrderNumber(999);
            order.setOrderDate(date);
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

            //Save the order
            dao.addOrder(order);

            //Retrieve the order
            Order retrievedOrder = dao.getOrder(date, 999);

            //Check the result
            assertNotNull(retrievedOrder);
            assertEquals(999, retrievedOrder.getOrderNumber());
            assertEquals("Test Customer", retrievedOrder.getCustomerName());
            assertEquals("TX", retrievedOrder.getState());
            assertEquals("Tile", retrievedOrder.getProductType());
            assertEquals(new BigDecimal("200"), retrievedOrder.getArea());
        }
}
