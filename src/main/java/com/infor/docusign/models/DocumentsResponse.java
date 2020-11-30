package com.infor.docusign.models;

import java.util.List;

public class DocumentsResponse {
    private List<Document> documents;
    private List<Template> templates;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }
}
