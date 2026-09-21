package com.mthree.dao;

import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;

import java.util.Map;

public class ExportDaoStubImpl implements ExportDao{
    /**
     * Accepts test orders without writing to a file.
     */
    @Override
    public void exportData(Map<Integer, Order> orders)
            throws PersistenceException {

        // No file writing is needed for service testing
    }
}
