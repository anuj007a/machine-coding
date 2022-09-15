package com.uber.consumer;

import com.uber.MessageRouter;
import com.uber.message.Message;
import com.uber.storage.MetaDataHolder;

import java.util.List;

public class ConsumerImpl implements Consumer {

    private String name;
    private Integer fetchSize;
    private String channel;
    private MessageRouter messageRouter;
    private MetaDataHolder metaDataHolder;

    public ConsumerImpl(String name, String channel, Integer fetchSize) {
        this.name = name;
        this.channel = channel;
        this.fetchSize = fetchSize;
        this.messageRouter = MessageRouter.getInstance();
        this.metaDataHolder = MetaDataHolder.getInstance();
        if (!messageRouter.isValidChannel(channel)) {
            throw new IllegalArgumentException("Invalid channel");
        }
        metaDataHolder.registerConsumer(this);
    }

    @Override
    public List<Message> fetchMessages() {
        return messageRouter.consume(this, channel, fetchSize);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void updateOffset(Integer offset) {
        messageRouter.updateOffset(this, offset);
    }

}
