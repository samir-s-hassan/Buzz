package edu.lehigh.cse216.pioneers.backend;

/**
 * SimpleUserRequest provides a format for clients to present user information to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class SimpleUserRequest {
    // public int mUserId;
    public String mUsername;
    public String mEmail;
    public String mGenderIdentity;
    public String mSexualOrientation;
    public String mBio;
    public boolean mValid;
}