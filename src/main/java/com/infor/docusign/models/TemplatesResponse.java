package com.infor.docusign.models;

import java.util.List;

public class TemplatesResponse {
    private Boolean authorized;
    private String loginUrl;
    private List<Template> templates;

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    public void setTemplate(Template template) {
        this.templates.add(template);
    }
}
