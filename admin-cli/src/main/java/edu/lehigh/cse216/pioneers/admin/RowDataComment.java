package edu.lehigh.cse216.pioneers.admin;

import java.sql.Date;

public class RowDataComment {
    
    int mCommentid;
    int mUserid;
    int mPostid;
    String mContent;
    Date mCreated;
    String mAttachment;
    String mLink;

    public RowDataComment(int commentId, int userId, int postId, String content, Date created, String attachment, String link){
        mCommentid =commentId;
        mUserid = userId;
        mPostid = postId;
        mContent = content;
        mCreated = created;
        mAttachment = attachment; 
        mLink = link; 
    }

}
