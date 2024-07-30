package com.anuj.model;

public class Containers {

    private int containerId;
    private int length;
    private int breath;
    private int height;


    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
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

    public Containers(int containerId, int length, int breath, int height) {
        this.containerId = containerId;
        this.length = length;
        this.breath = breath;
        this.height = height;
    }

    public int volume() {
        return length * breath * height;
    }
}
