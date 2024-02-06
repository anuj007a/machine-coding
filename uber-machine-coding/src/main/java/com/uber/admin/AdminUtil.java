package com.uber.admin;

import com.uber.storage.InMemoryStore;
import com.uber.storage.MetaDataHolder;

public class AdminUtil {
    private MetaDataHolder metaDataHolder;
    private InMemoryStore inMemoryStore;

    public AdminUtil() {
        metaDataHolder = MetaDataHolder.getInstance();
        inMemoryStore = InMemoryStore.getInstance();
    }

    public void addChannel(String channel) {
        metaDataHolder.addChannel(channel);
        inMemoryStore.addChannel(metaDataHolder.getChannel(channel));
    }
}
