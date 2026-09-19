package com.mthree.dao;

import com.mthree.model.Order;

import java.util.Map;

public interface ExportDao {
    public Map<Integer, Order> exportData();
}
