package edu.lehigh.cse216.pioneers.backend;

/**
 * SimplePostRequest provides a format for clients to present idea in string and likes count in integer to the server.
 * 
 * NB: since this will be created from JSON, all fields must be public, and we
 *     do not need a constructor.
 */
public class SimplePostRequest {

    /**
     * The user id being provided by the client.
     */
    public int mUserId;
    /**
     * The idea being provided by the client.
     */
    public String mIdea;

    /**
     * The likes being provided by the client.
     */
    public int mLikes;

    public boolean mValid;
}