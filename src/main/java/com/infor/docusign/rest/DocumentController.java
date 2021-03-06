package com.infor.docusign.rest;

import com.infor.daf.icp.*;
import com.infor.daf.icp.signature.v1.Signature;
import com.infor.daf.icp.signature.v1.SignatureEnvelope;
import com.infor.docusign.models.*;
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
            CMItems cmItems = CMItems.search(connection, "/Oleg_Test[@Purpose = \"POC\"]", 0, 10);
            for (CMItem item : cmItems) {
                Document document = new Document();
                document.setPid(item.getPid());
                document.setAuthor(item.getCreatedByName());
                document.setFileName(item.getFilename());
                document.setAcl(item.getAccessControlList().getName());
                document.setAttributes(new ArrayList<>());
                for (CMResource cmResource : item.getResources().values()) {
                    if (cmResource.getName().equals("Thumbnail")) {
                        Resource resource = new Resource();
                        resource.setSize(cmResource.getSize());
                        resource.setMime(cmResource.getMimeType());
                        resource.setFileName(cmResource.getMimeType());
                        resource.setUrl(cmResource.getUrl());
                        document.setResource(resource);
                    }
                }
                for (CMAttribute cmAttribute : item.getAttributes().values()) {
                    if (cmAttribute.getName().equals("Name")) {
                        Attribute attribute = new Attribute();
                        attribute.setName(cmAttribute.getName());
                        attribute.setType(cmAttribute.getType().toString());
                        attribute.setValue(cmAttribute.getValue() == null ? null : cmAttribute.getValue().toString());
                        document.getAttributes().add(attribute);
                    }
                }
                document.setEnvelopes(new ArrayList<>());
                SignatureEnvelope.EnvelopeStatus[] envelopeStatuses = Signature.getEnvelopeByPid(connection, document.getPid());
                for (SignatureEnvelope.EnvelopeStatus status : envelopeStatuses) {
                    SignatureEnvelope.EnvelopeDetail envelopeDetail = Signature.getEnvelopeById(connection, status.getSignatureId());
                    Envelope envelope = new Envelope();
                    envelope.setSignature(envelopeDetail.getSignatureId());
                    envelope.setStatus(envelopeDetail.getStatus());
                    envelope.setSubject(envelopeDetail.getSubject());
                    envelope.setMessage(envelopeDetail.getMessage());
                    envelope.setRecipients(envelopeDetail.getRecipients());
                    envelope.setDocuments(envelopeDetail.getDocuments());
                    document.setEnvelope(envelope);
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
