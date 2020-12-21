package com.infor.docusign.models;

public class SeedDocumentsRequest {

    private int count = 10;
    private String entityName;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
