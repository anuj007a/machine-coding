package com.wraith.impl;

import com.wraith.model.Containers;
import com.wraith.model.Order;
import com.wraith.model.Orders;
import com.wraith.model.Products;
import com.wraith.service.ContainerAllocation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContainerAllocationImpl implements ContainerAllocation {

    Map<Integer, Integer> orderBasedVolume = new HashMap<>();
    public Integer canFit(List<Orders> totalOrders, Map<Integer, Products> products, List<Containers> containers){
        int totalRequiredVolume = 0;
        int requiredVolume = 0;
        //calculate the total volumes
        for(Orders orders : totalOrders){
            int perOrderVolume = 0;
            for ( Order order : orders.getOrder()){
                Products products1 = products.get(order.getProductId());
                if(products1 != null){
                    totalRequiredVolume += products1.volume()*order.getQuantity();
                    orderBasedVolume.put(orders.getOrderId(), perOrderVolume);
                }else {
                    System.out.println("Product id is not found");
                }
            }
        }

  /*  for ( Order order : od){
        Products products1 = products.get(order.getProductId());
        if(products1 != null){
            requiredVolume += products1.volume()*order.getQuantity();
        }else {
            System.out.println("Product id is not found");
            //throe exception
        }
    }*/
        // sort the container as per size
        containers.sort(Comparator.comparing(Containers::volume));

    for(Containers container : containers){
        if( container.volume() >= totalRequiredVolume){
            return container.getContainerId();
        }
        else{

        }
    }
        return null;
    }


}
