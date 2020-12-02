package com.infor.docusign.rest;

import com.infor.daf.icp.Connection;
import com.infor.daf.icp.internal.signature.SignatureException;
import com.infor.daf.icp.signature.v1.*;
import com.infor.docusign.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
public class SignatureController {

    @GetMapping("/templates")
    public ResponseEntity<TemplatesResponse> getTemplates() {
        Connection connection = ConnectionStorage.getConnection();
        if (connection == null) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

        TemplatesResponse response = new TemplatesResponse();
        response.setTemplates(new ArrayList<>());
        response.setLoginUrl("");

        try {
            SignatureResponse<SignatureListTemplatesResponse> templatesResponse = Signature.listTemplates(connection);
            if (templatesResponse.getStatus().isAuthorized()) {
                for (SignatureTemplate.TemplateItem templateItem : templatesResponse.getData().getTemplateList()) {
                    SignatureResponse<SignatureTemplate> templateDetail = Signature.getTemplate(connection, templateItem.getTemplateId());
                    Template template = new Template();
                    template.setName(templateDetail.getData().getName());
                    template.setFilename(templateDetail.getData().getContents().get(0).getFile());
                    template.setExtension(templateDetail.getData().getContents().get(0).getExt());
                    template.setData(templateDetail.getData().getContents().get(0).getData());
                    response.setTemplate(template);
                }
                response.setAuthorized(true);
                return ResponseEntity.ok(response);
            } else {
                response.setAuthorized(false);
                response.setLoginUrl(templatesResponse.getStatus().getRedirect());
                return new ResponseEntity<>(response, UNAUTHORIZED);
            }
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
            SignatureResponse<SignatureEnvelope.EnvelopeStatus> sendStatus = Signature.sendEnvelope(connection, false, signatureEnvelope);
            if (sendStatus.getStatus().isAuthorized()) {
                response.setAuthorized(true);
                response.setLoginUrl("");
                response.setEnvelope(sendStatus.getData());
                return ResponseEntity.ok(response);
            } else {
                response.setAuthorized(false);
                response.setLoginUrl(sendStatus.getStatus().getRedirect());
                return new ResponseEntity<>(response, UNAUTHORIZED);
            }
        } catch (SignatureException | IOException e) {
            e.printStackTrace();
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
