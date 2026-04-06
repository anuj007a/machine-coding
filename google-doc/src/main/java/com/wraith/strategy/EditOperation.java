package com.wraith.strategy;

public interface EditOperation {
    String apply(String currentContent, String newContent);

}
