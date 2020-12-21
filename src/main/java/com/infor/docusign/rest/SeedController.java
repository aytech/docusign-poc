package com.infor.docusign.rest;

import com.infor.daf.icp.*;
import com.infor.docusign.models.SeedDocumentsRequest;
import org.springframework.http.ResponseEntity;
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

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class SeedController {

    @PostMapping("/seed/documents/add")
    public ResponseEntity<Boolean> seedDocuments(@RequestBody SeedDocumentsRequest request) {

        Connection connection = ConnectionStorage.getConnection();

        for (int i = 0; i < request.getCount(); i++) {
            try {
                addDocument(connection, request);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
            }
        }

        return ResponseEntity.ok(true);
    }

    private void addDocument(
            Connection connection,
            SeedDocumentsRequest request
    ) throws ParserConfigurationException, URISyntaxException, IOException, CMException, SAXException {
        URL cMItemResource = getClass().getClassLoader().getResource("data/CMItem.xml");
        URL cMResource = getClass().getClassLoader().getResource("data/prague1.jpg");
        if (cMItemResource != null && cMResource != null) {

            CMItem cmItem = getCMItem(cMItemResource.toURI());

            // Override document name
            cmItem.setEntityName(request.getEntityName());
            // Add file
            cmItem.getResources().add(getResource(cMResource.toURI()));
            // Persist item
            cmItem.add(connection);
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
