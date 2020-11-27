package com.infor.docusign.rest;

import com.infor.daf.icp.CMException;
import com.infor.daf.icp.Connection;

public class ConnectionStorage {

    private static Connection connection;

    private ConnectionStorage() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            connection = new Connection(
                    System.getenv("BASE_URL"),
                    System.getenv("CONSUMER_KEY"),
                    System.getenv("CLIENT_SECRET"),
                    Connection.AuthenticationMode.OAUTH1
            );
            connection.setTenant(System.getenv("TENANT_ID"));
            connection.setUsername(System.getenv("CLIENT_USER"));
            try {
                connection.connect();
            } catch (CMException e) {
                e.printStackTrace();
                return null;
            }
        }
        return connection;
    }
}
