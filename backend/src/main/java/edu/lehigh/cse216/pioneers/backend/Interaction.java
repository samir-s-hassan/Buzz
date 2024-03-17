package edu.lehigh.cse216.pioneers.backend;

public class Interaction {
    public int userId;
    public int postId;
    // -1 for downvote, 0 for no vote, 1 for upvote
    public int interactionType;

    Interaction(int userId, int postId, int interactionType) {
        this.userId = userId;
        this.postId = postId;
        this.interactionType = interactionType;
    }
}