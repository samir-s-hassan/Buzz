package edu.lehigh.cse216.pioneers.backend;

class SimpleInteractionRequest {
    /**
     * The user id being provided by the client.
     */
    public int mUserId;
    /**
     * -1 for downvote 0 for neutral 1 for up
     */
    public int mInteraction;
}