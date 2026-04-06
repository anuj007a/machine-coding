package com.wraith.service.impl;

import com.wraith.enums.DocumentType;
import com.wraith.model.Document;
import com.wraith.model.Version;
import com.wraith.repository.DocumentRepository;
import com.wraith.service.DocumentService;
import com.wraith.strategy.EditOperation;

import java.util.List;

public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository repository;

    public DocumentServiceImpl(DocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Document createDocument(String id, String documentName, DocumentType documentType, String content) {
        Document doc = new Document(id, documentName, documentType, content);
        repository.save(doc);
        return doc;
    }

    @Override
    public String getContent(String docId) {
        return repository.fingByDocumentId(docId).orElseThrow(() -> new RuntimeException("Document not found")).getContent();
    }

    @Override
    public void editDocument(String docId, String newContent, EditOperation operation) {
        Document doc = repository.fingByDocumentId(docId).orElseThrow(() -> new RuntimeException("Document not found"));
        String updated = operation.apply(doc.getContent(), newContent);
        doc.applyEdit(updated);

    }

    @Override
    public List<String> getHistory(String docId) {
        Document doc = repository.fingByDocumentId(docId).orElseThrow(() -> new RuntimeException("Document not found"));
        return doc.getVersions().stream().map(Version::getContent).toList();

    }

}
