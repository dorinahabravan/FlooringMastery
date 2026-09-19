package com.mthree.dao;

import com.mthree.model.Order;

import java.util.List;
import java.util.Map;

public class ExportDaoFileImpl implements ExportDao{


    private List<Order> allOrders;
    private static final String DELIMITER = ",";
    private static final String EXPORT_FILE = "Backup/DataExport.txt";

    private void writeToFile(){

    }
    @Override
    public Map<Integer, Order> exportData() {
        return Map.of();
    }
}
