package edu.lehigh.cse216.pioneers.backend;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import spark.*;


public class Authentication {
    public static final String CLIENT_ID = "64782561268-bm9vevggd5nq6ocsakie7ac9p5jh28vo.apps.googleusercontent.com";
    public static GoogleIdToken.Payload authenticateToken(String idTokenString)throws GeneralSecurityException , IOException {
        NetHttpTransport transport = new NetHttpTransport();
        GsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience(Collections.singletonList(CLIENT_ID))
        // Or, if multiple clients access the backend:
        //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
        .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            return idToken.getPayload();
        }
        else {
            throw new GeneralSecurityException("Invalid token");
        }
    }
}