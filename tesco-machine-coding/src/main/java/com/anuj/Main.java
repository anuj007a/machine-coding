package com.anuj;

import com.anuj.impl.ContainerAllocationImpl;
import com.anuj.model.Containers;
import com.anuj.model.Order;
import com.anuj.model.Orders;
import com.anuj.model.Products;
import com.anuj.service.ContainerAllocation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
/*
productId=1, quantity=3
productId=3, quantity=7
 */
        ContainerAllocation containerAllocation = new ContainerAllocationImpl();
        List<Containers> containers = Arrays.asList(
                new Containers(1, 10, 20, 30),
                new Containers(2, 50, 60, 70),
                new Containers(3, 100, 200, 300)
        );
        Map<Integer, Products> products = new HashMap<>();
        products.put(1, new Products(1, 2, 4, 10));
        products.put(2, new Products(2, 10, 30, 40));
        products.put(3, new Products(3, 5, 6, 7));

        List<Order> Order = Arrays.asList(
                new Order(1, 3)
        );

        List<Order> Order2 = Arrays.asList(
                new Order(2, 3)
        );

        List<Order> Order3 = Arrays.asList(
                new Order(2, 3),
                new Order(2, 3)
        );

        List<Orders> totalOrder = Arrays.asList(
                new Orders(1, Order),
                new Orders(2, Order2),
                new Orders(3, Order3)

        );

        Integer containerId = containerAllocation.canFit(totalOrder, products, containers);
        if (containerId != null) {
            System.out.println("Container id for order is " + containerId);
        } else {
            System.out.println("Order is not fit for any container");
        }
    }
}
