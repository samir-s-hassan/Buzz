package edu.lehigh.cse216.pioneers.backend;
import java.util.Date;

class Comment {
   public int mCommentId;
   public int mUserId;
   public int mPostId;
   public String mContent;
   public Date mDate;

    Comment(int commentId, int userId, int postId, String content, Date date) {
        mCommentId = commentId;
        mUserId = userId;
        mPostId = postId;
        mContent = content;
        mDate = date;
    }

    Comment(int userId, int postId, String content, Date date) {
        this.mUserId = userId;
        this.mPostId = postId;
        this.mContent = content;
        this.mDate = date;
    }
}