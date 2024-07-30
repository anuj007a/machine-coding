package com.anuj.service;

import com.anuj.model.Containers;
import com.anuj.model.Order;
import com.anuj.model.Orders;
import com.anuj.model.Products;
import com.sun.source.tree.ContinueTree;

import java.util.List;
import java.util.Map;

public interface ContainerAllocation {

    public Integer canFit(List<Orders> totalOrders, Map<Integer, Products> products, List<Containers> container);
}
