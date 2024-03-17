package edu.lehigh.cse216.pioneers.backend;

import java.util.Date;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;


/**
 * Unit test for simple App.
 */
public class PostTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PostTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PostTest.class);
    }

    /**
     * Ensure that the constructor populates every field of the object it
     * creates
     */
    public void testConstructor() {
        String idea = "Test Title";
        int likes = 12;
        int id = 17;
    int[] comments = {1,2,3};
        int userId = 420;
        Date created = new Date(); // Current date and time
        Post d = new Post(id, userId, idea, likes, created, true); 

        assertTrue(d.mUserId == userId);
        assertTrue(d.mIdea.equals(idea));
        assertTrue(d.mLikes == likes);
        assertTrue(d.mId == id);
        //assertFalse(d.mCreated == null);
    }

    /**
     * Ensure that the copy constructor works correctly
     */
    public void testCopyconstructor() {
        String idea = "Test Title for Copy";
        int likes = 24;
        int id = 177;
        int userId = 80;
        Date created = new Date(); // Current date and time
        Post d = new Post(id,userId  ,idea, likes, created, true);
        Post d2 = new Post(d);
        assertTrue(d2.mUserId == d.mUserId);
        assertTrue(d2.mIdea.equals(d.mIdea));
        assertTrue(d2.mLikes == d.mLikes);
        assertTrue(d2.mId == d.mId);
        //assertTrue(d2.mCreated.equals(d.mCreated));
    }
}