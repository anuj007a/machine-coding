package com.wraith;

import com.wraith.enums.DocumentType;
import com.wraith.repository.DocumentRepository;
import com.wraith.service.DocumentService;
import com.wraith.service.impl.DocumentServiceImpl;import com.wraith.strategy.ReplaceOperation;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class DocumentDriver {
    public static void main(String[] args) {
        DocumentRepository  repo = new DocumentRepository();
        DocumentService service = new DocumentServiceImpl(repo);

        service.createDocument("1", "Doc1", DocumentType.PII, "Hello World");
        System.out.println(service.getContent("1")); // Hello World

        service.editDocument("1", "New Content", new ReplaceOperation());
        System.out.println(service.getContent("1")); // Hello World
        System.out.println(service.getHistory("1"));

        try {
            service.getContent("5");
        }
        catch (Exception e) {
            System.out.println(e.getMessage()); // Document not found

    }
    }
}