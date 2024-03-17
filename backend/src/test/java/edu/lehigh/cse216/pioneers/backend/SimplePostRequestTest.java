package edu.lehigh.cse216.pioneers.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for SimpleRequest.
 */
public class SimplePostRequestTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SimplePostRequestTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(SimplePostRequestTest.class);
    }

    /**
     * Test the SimpleRequest fields.
     */
    public void testSimpleRequestFields() {
        int userId = 1;
        String idea = "Test idea";
        int likes = 10;
        SimplePostRequest simpleRequest = new SimplePostRequest();
        simpleRequest.mIdea = idea;
        simpleRequest.mLikes = likes;
        simpleRequest.mUserId = userId;

        assertEquals(simpleRequest.mUserId, userId);
        assertEquals(simpleRequest.mIdea, idea);
        assertEquals(simpleRequest.mLikes, likes);
    }
}
