package com.infor.docusign.rest;

import com.infor.daf.icp.Connection;
import com.infor.daf.icp.internal.signature.SignatureException;
import com.infor.daf.icp.signature.v1.*;
import com.infor.docusign.models.Recipient;
import com.infor.docusign.models.SignDocumentsRequest;
import com.infor.docusign.models.SignDocumentsResponse;
import com.infor.docusign.models.Template;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class SignatureController {

    @GetMapping("/templates")
    public ResponseEntity<List<Template>> getTemplates() {
        Connection connection = ConnectionStorage.getConnection();
        if (connection == null) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

        try {
            List<Template> templates = new ArrayList<>();
            SignatureResponse<SignatureListTemplatesResponse> templatesData = Signature.listTemplates(connection);
            for (SignatureTemplate.TemplateItem templateItem : templatesData.getData().getTemplateList()) {
                SignatureResponse<SignatureTemplate> templateDetail = Signature.getTemplate(connection, templateItem.getTemplateId());
                Template template = new Template();
                template.setName(templateDetail.getData().getName());
                template.setFilename(templateDetail.getData().getContents().get(0).getFile());
                template.setExtension(templateDetail.getData().getContents().get(0).getExt());
                template.setData(templateDetail.getData().getContents().get(0).getData());
                templates.add(template);
            }
            return ResponseEntity.ok(templates);
        } catch (SignatureException e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/sign")
    public ResponseEntity<SignDocumentsResponse> signDocuments(
            @RequestBody SignDocumentsRequest request
    ) {
        Connection connection = ConnectionStorage.getConnection();
        if (connection == null) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

        SignDocumentsResponse response = new SignDocumentsResponse();
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
            response.setEnvelope(Signature.sendEnvelope(connection, false, signatureEnvelope));
            response.setStatus(true);
            return ResponseEntity.ok(response);
        } catch (SignatureException | IOException e) {
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, BAD_REQUEST);
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
