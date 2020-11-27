package com.infor.docusign.models;

import java.util.UUID;

public class StatusRequest {
    private UUID signature;

    public UUID getSignature() {
        return signature;
    }

    public void setSignature(UUID signature) {
        this.signature = signature;
    }
}
