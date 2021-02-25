package com.infor.docusign.models;

public class SeedDocumentsRequest {

    private int count = 10;
    private int threads = 10;
    private int timeout = 1;
    private String entityName;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getTimeout() {
        return timeout * 60000;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
