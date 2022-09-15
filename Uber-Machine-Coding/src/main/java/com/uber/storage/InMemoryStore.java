package com.uber.storage;

import com.uber.channel.Channel;
import com.uber.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryStore implements StorageEngine {

    private Map<Channel, List<Message>> channelToMessages;

    private static InMemoryStore instance;

    private InMemoryStore() {
        this.channelToMessages = new HashMap<>();
    }

    public static InMemoryStore getInstance() {
        if (instance == null) {
            instance = new InMemoryStore();
        }
        return instance;
    }

    @Override
    public boolean publish(Channel channel, Message message) {
        synchronized (channel) {
            channelToMessages.get(channel).add(message);
        }
        return true;
    }

    @Override
    public void addChannel(Channel channel) {
        channelToMessages.put(channel, new ArrayList<>());
    }

    @Override
    public List<Message> getMessages(Channel channel, Integer offset, Integer size) {
        List<Message> msg = new ArrayList<>();
        while (size > 0 && channelToMessages.get(channel).size() > offset) {
            msg.add(channelToMessages.get(channel).get(offset));
            offset++;
            size--;
        }
        return msg;
    }
}
