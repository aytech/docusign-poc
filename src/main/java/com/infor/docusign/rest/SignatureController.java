package com.infor.docusign.rest;

import com.infor.daf.icp.Connection;
import com.infor.daf.icp.internal.signature.SignatureException;
import com.infor.daf.icp.signature.v1.*;
import com.infor.docusign.models.Recipient;
import com.infor.docusign.models.SignDocumentsRequest;
import com.infor.docusign.models.Template;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class SignatureController {

    @GetMapping("/templates")
    public ResponseEntity<SignatureResponse<SignatureListTemplatesResponse>> getTemplates() {
        Connection connection = ConnectionStorage.getConnection();
        if (connection == null) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

        try {
            return ResponseEntity.ok(Signature.listTemplates(connection));
        } catch (SignatureException e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/templates/{id}")
    public ResponseEntity<SignatureResponse<SignatureTemplate>> getTemplate(@PathVariable UUID id) {
        Connection connection = ConnectionStorage.getConnection();
        if (connection == null) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
        try {
            return ResponseEntity.ok(Signature.getTemplate(connection, id.toString()));
        } catch (SignatureException e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/sign")
    public ResponseEntity<SignatureResponse<SignatureEnvelope.EnvelopeStatus>> signDocuments(
            @RequestBody SignDocumentsRequest request
    ) {
        Connection connection = ConnectionStorage.getConnection();
        if (connection == null) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

        SignatureEnvelope signatureEnvelope = new SignatureEnvelope();
        List<String> documents = new ArrayList<>(request.getPids());
        List<SignatureEnvelope.Recipient> signers = new ArrayList<>();
        List<SignatureEnvelope.Content> contents = new ArrayList<>();

        for (Recipient recipient : request.getRecipients()) {
            SignatureEnvelope.Recipient signer = new SignatureEnvelope.Recipient();
            signer.setName(recipient.getName());
            signer.setEmail(recipient.getEmail());
            signers.add(signer);
        }
        signatureEnvelope.setRecipients(signers);

        for (Template template : request.getTemplates()) {
            SignatureEnvelope.Content content = new SignatureEnvelope.Content();
            content.setId(String.valueOf(Math.round(Math.random() * 10))); // This can't be template ID, but random number
            content.setFile(template.getFilename());
            content.setExt(template.getExtension());
            content.setData(template.getData());
            contents.add(content);
        }
        signatureEnvelope.setContents(contents);

        signatureEnvelope.setSubject(request.getSubject());
        signatureEnvelope.setMessage(request.getMessage());
        signatureEnvelope.setItems(documents);

        try {
            return ResponseEntity.ok(Signature.sendEnvelope(connection, false, signatureEnvelope));
        } catch (SignatureException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{signature}")
    public ResponseEntity<SignatureEnvelope.EnvelopeDetail> getEnvelopeStatus(@PathVariable String signature) {
        Connection connection = ConnectionStorage.getConnection();
        if (connection == null) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
        try {
            System.out.println("Request: " + signature);
            return ResponseEntity.ok(Signature.getEnvelopeById(connection, signature));
        } catch (SignatureException e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}
