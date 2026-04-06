package org.wraith.strategy;

public class ReplaceOperation implements EditOperation {
    @Override
    public String apply(String currentContent, String newContent) {
        return newContent;
    }
}
