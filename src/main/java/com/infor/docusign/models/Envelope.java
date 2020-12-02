package com.infor.docusign.models;

import com.infor.daf.icp.signature.v1.SignatureEnvelope;

import java.util.List;

public class Envelope {
    private String signature;
    private String status;
    private String subject;
    private String message;
    private List<SignatureEnvelope.Recipient> recipients;
    private List<SignatureEnvelope.Document> documents;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SignatureEnvelope.Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<SignatureEnvelope.Recipient> recipients) {
        this.recipients = recipients;
    }

    public List<SignatureEnvelope.Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<SignatureEnvelope.Document> documents) {
        this.documents = documents;
    }
}
