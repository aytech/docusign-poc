package com.infor.docusign.models;

import com.infor.daf.icp.signature.v1.SignatureEnvelope;
import com.infor.daf.icp.signature.v1.SignatureResponse;

public class SignDocumentsResponse {
    private SignatureResponse<SignatureEnvelope.EnvelopeStatus> envelope;
    private String message;
    private Boolean status;

    public SignatureResponse<SignatureEnvelope.EnvelopeStatus> getEnvelope() {
        return envelope;
    }

    public void setEnvelope(SignatureResponse<SignatureEnvelope.EnvelopeStatus> envelope) {
        this.envelope = envelope;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
