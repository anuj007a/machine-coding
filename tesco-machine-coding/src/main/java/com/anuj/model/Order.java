package com.anuj.model;

import java.util.List;

public class Order {
// Order will contain the list of multiple item => i item is eail ot 1 product
    private int productId;
    private int quantity;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
