package com.mthree.dao;

import com.mthree.exceptions.PersistenceException;
import com.mthree.model.Order;

import java.util.Map;

public interface ExportDao {

    public void exportData(Map<Integer, Order> orders) throws PersistenceException;
}
