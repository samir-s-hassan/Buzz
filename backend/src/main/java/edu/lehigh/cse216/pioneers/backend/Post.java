package edu.lehigh.cse216.pioneers.backend;

import java.sql.Array;
import java.util.Date;

/**
 * Post holds a row of information.  A row of information consists of
 * an identifier, strings for a "title" and "content", and a creation date.
 * 
 * Because we will ultimately be converting instances of this object into JSON
 * directly, we need to make the fields public.  That being the case, we will
 * not bother with having getters and setters... instead, we will allow code to
 * interact with the fields directly.
 */
public class Post {
    /**
     * The unique identifier associated with this element.  It's final, because
     * we never want to change it.
     */
    public final int mId;

    public final int mUserId;
    /**
     * The idea for this row of data
     */
    public String mIdea;

    /**
     * The likes for this row of data
     */
    public int mLikes;
    /**
     * The creation date for this row of data.  Once it is set, it cannot be 
     * changed
     */
    public Date mCreated;

    public boolean mValid;

    /**
     * Create a new Post with the provided id and title/content, and a 
     * creation date based on the system clock at the time the constructor was
     * called
     * 
     * @param id The id to associate with this row.  Assumed to be unique 
     *           throughout the whole program.
     * 
     * @param content The content string for this row of data
     * @param likes The likes for this row of data
     */
    Post(int id,int userId , String idea, int likes,Date created,boolean valid ) {
        mId = id;
        mUserId = userId;
        //mTitle = title;
        mIdea = idea;
        mLikes = likes;
        mCreated = created;
        mValid = valid;
    }

    /**
     * Copy constructor to create one Post from another
     */
    Post(Post data) {
        mId = data.mId;
        mUserId = data.mUserId;
        // NB: Strings and Dates are immutable, so copy-by-reference is safe
        //mTitle = data.mTitle;
        mIdea = data.mIdea;
        mLikes = data.mLikes;
        mCreated = data.mCreated;
        mValid = data.mValid;
    }
}