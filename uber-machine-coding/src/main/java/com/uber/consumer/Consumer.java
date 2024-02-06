package com.uber.consumer;

import com.uber.message.Message;

import java.util.List;

public interface Consumer {

    List<Message> fetchMessages();
    String name();
    void updateOffset(Integer offset);
}
