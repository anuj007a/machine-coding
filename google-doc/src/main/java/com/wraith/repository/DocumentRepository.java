package org.wraith.repository;

import org.wraith.model.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DocumentRepository {
    private final Map<String, Document> store = new HashMap<>();
    public void save(Document doc) {
        store.put(doc.getDocumentId(), doc);
    }

    public Optional<Document> fingByDocumentId(String documentId) {
        return Optional.ofNullable(store.get(documentId));
    }
}
