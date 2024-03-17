package edu.lehigh.cse216.pioneers.admin;

public class RowDataInteraction {
    int mInteractionID; 
    int mUserID;
    int mPostID;
    int mInteractionType;  

    public RowDataInteraction(int interactionID, int UserID, int postID, int interactionType){
        mInteractionID = interactionID;
        mUserID = UserID;
        mPostID = postID; 
        mInteractionID = interactionType; 
    }
}
