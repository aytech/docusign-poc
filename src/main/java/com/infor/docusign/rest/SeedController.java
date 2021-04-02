package com.infor.docusign.rest;

import com.infor.daf.icp.*;
import com.infor.docusign.models.SeedDocumentsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RestController
public class SeedController {

    @PostMapping("/seed/documents/add")
    public ResponseEntity<Boolean> seedDocuments(@RequestBody SeedDocumentsRequest request) {

        System.out.printf(
                "Running %s threads to add %s documents to %s, with %s minute timeout\n",
                request.getThreads(),
                request.getCount(),
                request.getEntityName(),
                request.getTimeout() / 60000
        );

        Connection connection = ConnectionStorage.getConnection();
        List<Thread> threadPool = new ArrayList<>();

        Runnable process = () -> {
            for (int i = 0; i < request.getCount(); i++) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.printf("%s is terminated\n", Thread.currentThread().getName());
                    break;
                }
                try {
                    addDocument(connection, request);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    System.out.println("Error saving document: " + e.getMessage());
                }
            }
        };

        for (int i = 0; i < request.getThreads(); i++) {
            Thread thread = new Thread(process);
            thread.start();
            threadPool.add(thread);
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < request.getThreads(); i++) {
                    threadPool.get(i).interrupt();
                }
            }
        }, request.getTimeout());

        return ResponseEntity.ok(true);
    }

    @GetMapping("/seed/entities/clean")
    public ResponseEntity<Boolean> cleanEntities() {
        Connection connection = ConnectionStorage.getConnection();
        try {
            CMEntities entities = CMEntities.getEntities(connection);
            System.out.println("Entities: ");
            System.out.println(entities);
        } catch (CMException e) {
            System.out.println("Error fetching entities: " + e.getMessage());
            e.printStackTrace();
        }
        return ResponseEntity.ok(true);
    }

    private void addDocument(
            Connection connection,
            SeedDocumentsRequest request
    ) throws ParserConfigurationException, URISyntaxException, IOException, CMException, SAXException {
        URL cMItemResource = getClass().getClassLoader().getResource("data/CMItem.xml");
        // URL cMResource = getClass().getClassLoader().getResource("data/prague1.jpg");
        if (cMItemResource != null) {

            CMItem cmItem = getCMItem(cMItemResource.toURI());

            // Override document name
            cmItem.setEntityName(request.getEntityName());
            // Add file
            // cmItem.getResources().add(getResource(cMResource.toURI()));
            // Persist item
            cmItem.add(connection);
            System.out.printf("Added document %s for document type %s%n", cmItem.getPid(), cmItem.getEntityName());
        }
    }

    private CMItem getCMItem(URI resourceUri) throws ParserConfigurationException, IOException, SAXException, CMException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(new File(resourceUri));
        CMItem cmItem = new CMItem();
        cmItem.fromXml(document);
        return cmItem;
    }

    private CMResource getResource(URI resourceUri) throws IOException {
        CMResource resource = new CMResource();
        File resourceFile = new File(resourceUri);
        resource.setFilename(resourceFile.getName());
        resource.setByteArray(Files.readAllBytes(resourceFile.toPath()));
        return resource;
    }
}
