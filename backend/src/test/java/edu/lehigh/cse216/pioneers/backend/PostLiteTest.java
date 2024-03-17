package edu.lehigh.cse216.pioneers.backend;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;


/**
 * Unit test for PostLite.
 */
public class PostLiteTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PostLiteTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PostLiteTest.class);
    }

    /**
     * Test the PostLite constructor.
     */
    public void testConstructor() {
        int id = 1;
        int userId = 69;
        String idea = "Test idea";
        Post Post = new Post(id, userId, idea, 0,null, true);
        PostLite PostLite = new PostLite(Post);

        assertEquals(PostLite.mId, id);
        assertEquals(PostLite.mIdea, idea);
    }
}
