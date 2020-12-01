package com.infor.docusign.models;

import java.util.ArrayList;
import java.util.List;

public class Document {
    private String pid;
    private String author;
    private String fileName;
    private String acl;
    private Resource resource;
    private List<Attribute> attributes;
    private List<Envelope> envelopes;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Envelope> getEnvelopes() {
        return envelopes;
    }

    public void setEnvelopes(List<Envelope> envelopes) {
        this.envelopes = envelopes;
    }

    public void setEnvelope(Envelope envelope) {
        if (envelopes == null) {
            envelopes = new ArrayList<>();
        }
        this.envelopes.add(envelope);
    }
}
