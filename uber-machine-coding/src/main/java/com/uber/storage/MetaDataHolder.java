package com.uber.storage;

import com.uber.consumer.Consumer;
import com.uber.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class MetaDataHolder {

    private Map<String, Channel> channels;
    private Map<Consumer, Integer> consumerOffsets;

    public MetaDataHolder() {
        channels = new HashMap<>();
        consumerOffsets = new HashMap<>();
    }

    private static MetaDataHolder instance;

    public static MetaDataHolder getInstance() {
        if (instance == null) {
            instance = new MetaDataHolder();
        }
        return instance;
    }

    public void addChannel(String channel) {
        if (!channels.containsKey(channel)) {
            channels.put(channel, new Channel(channel));
        }
    }

    public Channel getChannel(String channel) {
        return channels.get(channel);
    }

    public void registerConsumer(Consumer consumer) {
        consumerOffsets.put(consumer, 0);
    }

    public void updateConsumerOffset(Consumer consumer, Integer offset) {
        consumerOffsets.put(consumer, offset);
    }

    public Integer getConsumerOffset(Consumer consumer) {
        return consumerOffsets.get(consumer);
    }
}
