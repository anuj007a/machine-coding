package com.wraith.strategy;

public class AppendOperation implements EditOperation {
    @Override
    public String apply(String currentContent, String newContent) {
        return currentContent + newContent;
    }
}
