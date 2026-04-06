package com.wraith.service;

import com.wraith.enums.DocumentType;
import com.wraith.model.Document;
import com.wraith.strategy.EditOperation;

import java.util.List;

public interface DocumentService {
    Document createDocument(String id, String documentName, DocumentType documentType, String content);
    String getContent(String docId);
    void editDocument(String docId, String newContent, EditOperation operation);
    List<String> getHistory(String docId);


}
