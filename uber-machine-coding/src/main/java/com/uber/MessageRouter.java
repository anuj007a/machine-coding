package com.uber;

import com.uber.consumer.Consumer;
import com.uber.message.Message;
import com.uber.storage.InMemoryStore;
import com.uber.storage.MetaDataHolder;
import com.uber.storage.StorageEngine;

import java.util.List;

public class MessageRouter {

    private StorageEngine storageEngine;
    private MetaDataHolder metaData;

    private MessageRouter() {
        this.storageEngine = InMemoryStore.getInstance();
        this.metaData = MetaDataHolder.getInstance();
    }

    private static MessageRouter instance;

    public static MessageRouter getInstance() {
        if (instance == null) {
            instance = new MessageRouter();
        }
        return instance;
    }

    public boolean publish(String channel, String message) {
        return storageEngine.publish(metaData.getChannel(channel), new Message(message));
    }

    public List<Message> consume(Consumer consumer, String channel, Integer size) {
        Integer currentOffset = metaData.getConsumerOffset(consumer);
        List<Message> messages = storageEngine.getMessages(metaData.getChannel(channel), currentOffset, size);
        metaData.updateConsumerOffset(consumer, currentOffset + messages.size());
        return messages;
    }

    public boolean isValidChannel(String channel) {
        return metaData.getChannel(channel) != null;
    }

    public void updateOffset(Consumer consumer, Integer offset) {
        metaData.updateConsumerOffset(consumer, offset);
    }
}
