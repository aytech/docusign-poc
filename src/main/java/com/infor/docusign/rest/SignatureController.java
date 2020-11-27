package com.infor.docusign.rest;

import com.infor.daf.icp.Connection;
import com.infor.daf.icp.internal.signature.SignatureException;
import com.infor.daf.icp.signature.v1.Signature;
import com.infor.daf.icp.signature.v1.SignatureEnvelope;
import com.infor.daf.icp.signature.v1.SignatureResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class SignatureController {

    @PostMapping("/sign")
    public ResponseEntity<SignatureResponse<SignatureEnvelope.EnvelopeStatus>> signDocuments() {
        Connection connection = ConnectionStorage.getConnection();
        if (connection == null) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

        SignatureEnvelope signatureEnvelope = new SignatureEnvelope();
        List<SignatureEnvelope.Recipient> recipients = new ArrayList<>();
        List<String> documents = new ArrayList<>();
        List<SignatureEnvelope.Content> contents = new ArrayList<>();

        documents.add("Oleg_Test-32-1-LATEST");

        SignatureEnvelope.Recipient recipient = new SignatureEnvelope.Recipient();
        recipient.setName("Oleg Yapparov");
        recipient.setEmail("Oleg.Yapparov@infor.com");
        recipients.add(recipient);

        signatureEnvelope.setSubject("Sample signature request - POC");
        signatureEnvelope.setMessage("DocuSign service for Infor.");
        signatureEnvelope.setRecipients(recipients);
        signatureEnvelope.setItems(documents);

        SignatureEnvelope.Content content = new SignatureEnvelope.Content();
        contents.add(content);
        signatureEnvelope.setContents(contents);

        try {
            SignatureResponse<SignatureEnvelope.EnvelopeStatus> sendEnvelopeResponse =
                    Signature.sendEnvelope(connection, false, signatureEnvelope);
            System.out.println("Response" + sendEnvelopeResponse);
            return new ResponseEntity<>(sendEnvelopeResponse, OK);
        } catch (SignatureException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}
