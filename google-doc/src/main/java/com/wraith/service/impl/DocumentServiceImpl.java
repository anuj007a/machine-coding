package org.wraith.service.impl;

import org.wraith.enums.DocumentType;
import org.wraith.model.Document;
import org.wraith.model.Version;
import org.wraith.repository.DocumentRepository;
import org.wraith.service.DocumentService;
import org.wraith.strategy.EditOperation;

import javax.print.Doc;
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
