package org.wraith.model;

import org.wraith.enums.DocumentType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Document {
    private final String documentId;
    private final String documentName;
    private final DocumentType documentType;
    private String content;
    private final List<Version> versions;
    private final ReentrantReadWriteLock lock;

    public Document(String documentId, String documentName, DocumentType documentType, String content) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.documentType = documentType;
        this.versions = new ArrayList<>();
        this.content = content;
        this.lock = new ReentrantReadWriteLock();
        saveVersion();
    }

    public String getDocumentId() {
        return documentId;
    }

    public void applyEdit(String newContent) {
        lock.writeLock().lock();
        try {
            saveVersion();
            this.content = newContent;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void saveVersion() {
        versions.add(new Version("v" + (versions.size() + 1), content, System.currentTimeMillis()));
    }

    public List<Version> getVersions() {
        return Collections.unmodifiableList(versions);
    }


    public String getContent() {
        lock.readLock().lock();
        try {
            return content;
        } finally {
            lock.readLock().unlock();
        }
    }
}
