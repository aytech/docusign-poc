package com.infor.docusign.models;

import com.infor.daf.icp.signature.v1.SignatureEnvelope;
import com.infor.daf.icp.signature.v1.SignatureResponse;

public class SignDocumentsResponse {
    private SignatureEnvelope.EnvelopeStatus envelope;
    private String loginUrl;
    private Boolean authorized;

    public SignatureEnvelope.EnvelopeStatus getEnvelope() {
        return envelope;
    }

    public void setEnvelope(SignatureEnvelope.EnvelopeStatus envelope) {
        this.envelope = envelope;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public Boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }
}
