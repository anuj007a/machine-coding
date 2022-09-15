package com.uber.producer;


import com.uber.MessageRouter;

public class ProducerImpl implements Producer {

    private MessageRouter messageRouter;
    private String channel;

    public ProducerImpl(String channel) {
        this.channel = channel;
        this.messageRouter = MessageRouter.getInstance();
        if (!this.messageRouter.isValidChannel(channel)) {
            throw new IllegalArgumentException("Invalid Channel");
        }
    }

    @Override
    public boolean publish(String message) {
        return messageRouter.publish(channel, message);
    }

    @Override
    public String getChannel() {
        return channel;
    }

}
