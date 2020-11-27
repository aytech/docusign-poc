package com.infor.docusign.rest;

import com.infor.daf.icp.*;
import com.infor.docusign.models.Attribute;
import com.infor.docusign.models.Document;
import com.infor.docusign.models.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class DocumentController {

    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getDocuments() {
        List<Document> documents = new ArrayList<>();
        Connection connection = ConnectionStorage.getConnection();

        if (connection == null) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

        try {
            CMItems cmItems = CMItems.search(connection, "/Oleg_Test[@Name = \"POC\"]", 0, 10);
            System.out.println(cmItems);
            for (CMItem item : cmItems) {
                Document document = new Document();
                document.setPid(item.getPid());
                document.setAuthor(item.getCreatedByName());
                document.setFileName(item.getFilename());
                document.setAcl(item.getAccessControlList().getName());
                document.setAttributes(new ArrayList<>());
                for (CMResource cmResource : item.getResources().values()) {
                    if (cmResource.getName().equals("Preview")) {
                        Resource resource = new Resource();
                        resource.setSize(cmResource.getSize());
                        resource.setMime(cmResource.getMimeType());
                        resource.setFileName(cmResource.getMimeType());
                        resource.setUrl(cmResource.getUrl());
                        document.setResource(resource);
                    }
                }
                for (CMAttribute cmAttribute : item.getAttributes().values()) {
                    Attribute attribute = new Attribute();
                    attribute.setName(cmAttribute.getName());
                    attribute.setType(cmAttribute.getType().toString());
                    attribute.setValue(cmAttribute.getValue() == null ? null : cmAttribute.getValue().toString());
                    document.getAttributes().add(attribute);
                }
                documents.add(document);
            }
            return ResponseEntity.ok(documents);
        } catch (CMException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
