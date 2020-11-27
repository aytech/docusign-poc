package com.infor.docusign.models;

import java.util.List;

public class SignDocumentsRequest {
    private String subject;
    private String message;
    private List<String> pids;
    private List<Template> templates;
    private List<Recipient> recipients;

    public List<String> getPids() {
        return pids;
    }

    public void setPid(List<String> pids) {
        this.pids = pids;
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

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }
}
