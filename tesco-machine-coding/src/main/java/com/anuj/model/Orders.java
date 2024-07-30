package com.anuj.model;

import java.util.List;

public class Orders {

    private List<Order> order;
    private int orderId;

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Orders(int orderId, List<Order> order) {
        this.order = order;
        this.orderId = orderId;
    }
}
