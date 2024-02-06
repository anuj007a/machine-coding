package com.uber.producer;

public interface Producer {

    boolean publish(String message);
    String getChannel();

}
