package com.infor.docusign.rest;

import com.infor.daf.icp.CMException;
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
import static org.springframework.http.HttpStatus.OK;

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

    @PostMapping("oauth2")
    public ResponseEntity<Boolean> authenticate2() {
        Connection connection = new Connection(
                System.getenv("BASE_URL"),
                System.getenv("CONSUMER_KEY"),
                System.getenv("CLIENT_SECRET"),
                Connection.AuthenticationMode.OAUTH1
        );
        connection.setTenant(System.getenv("TENANT_ID"));
        connection.setUsername(System.getenv("CLIENT_USER"));
        try {
            connection.connect();
            System.out.println(connection.getProperties());
            return new ResponseEntity<>(true, OK);
        } catch (CMException e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }
}
