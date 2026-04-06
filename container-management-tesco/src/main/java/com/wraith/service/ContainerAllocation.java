package com.wraith.service;

import com.wraith.model.Containers;
import com.wraith.model.Orders;
import com.wraith.model.Products;

import java.util.List;
import java.util.Map;

public interface ContainerAllocation {

    public Integer canFit(List<Orders> totalOrders, Map<Integer, Products> products, List<Containers> container);
}
