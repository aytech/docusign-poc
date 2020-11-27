package com.infor.docusign.rest;

import com.infor.daf.icp.Connection;
import com.infor.daf.icp.internal.signature.SignatureException;
import com.infor.daf.icp.signature.v1.Signature;
import com.infor.daf.icp.signature.v1.SignatureAuthorization;
import com.infor.docusign.models.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class AuthenticationController {
    @PostMapping("/authenticate")
    public ResponseEntity<SignatureAuthorization> authenticate(@RequestBody AuthRequest request) {
        Connection connection = ConnectionStorage.getConnection();

        if (connection == null) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

        try {
            return ResponseEntity.ok(Signature.loginAuthorization(connection, request.getCode(), "0"));
        } catch (SignatureException e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}
