package edu.lehigh.cse216.pioneers.admin;

import java.sql.Date;

/**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, RowData 
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public class RowDataPost {
        /**
         * The ID of this row of the database
         */
        int mPostId;
        /**
         * The id of the user who posted the post
         */
        int mUserId;
        /**
         * The message stored in this row
         */
        String mIdea;

        /**
         * The number of likes stored in this row
         */
        int mLikes;
        /**
         * The date and time that the post was created
         */
        Date mCreated;

        String mAttachment;

        String mLink;
        String mValid; 
        

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowDataPost(int postId, int userId, String idea, int likes, Date created, String valid, String attachment, String link){
            mPostId = postId;
            mUserId = userId;
            mIdea = idea;
            mLikes = likes;
            mCreated = created;
            mAttachment = attachment; 
            mLink = link;
            mValid = valid; 
        }
    }
