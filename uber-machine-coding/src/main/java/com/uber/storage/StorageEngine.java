package com.uber.storage;

import com.uber.channel.Channel;
import com.uber.message.Message;

import java.util.List;

public interface StorageEngine {

    boolean publish(Channel channel, Message message);
    void addChannel(Channel channel);
    List<Message> getMessages(Channel channel, Integer offset, Integer size);
}
