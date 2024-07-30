package com.anuj.model;

public class Products {

    private int productId;
    private int length;
    private int breath;
    private int height;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getBreath() {
        return breath;
    }

    public void setBreath(int breath) {
        this.breath = breath;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Products(int productId, int length, int breath, int height) {
        this.productId = productId;
        this.length = length;
        this.breath = breath;
        this.height = height;
    }

    public int volume() {
        return length * breath * height;
    }
}
