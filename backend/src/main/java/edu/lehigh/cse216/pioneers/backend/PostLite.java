package edu.lehigh.cse216.pioneers.backend;

/**
 * PostLite is for communicating back a subset of the information in a
 * Post.  Specifically, we only send back the id and idea.  Note that
 * in order to keep the client code as consistent as possible, we ensure 
 * that the field names in PostLite match the corresponding names in
 * Post.  As with Post, we plan to convert PostLite objects to
 * JSON, so we need to make their fields public.
 * 
 * CHANGED for mMessage to mIdea
 */
public class PostLite {
    /**
     * The id for this row; see Post.mId
     */
    public int mId;

    /**
     * The idea string for this row of data; see Post.mIdea
     */
    public String mIdea;

    /**
     * Create a PostLite by copying fields from a Post
     * @param data The Post object from which to copy fields
     */
    public PostLite(Post data) {
        this.mId = data.mId;
        this.mIdea = data.mIdea;
    }
}