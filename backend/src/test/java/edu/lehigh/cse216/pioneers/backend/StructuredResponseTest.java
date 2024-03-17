package edu.lehigh.cse216.pioneers.backend;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for StructuredResponse.
 */
public class StructuredResponseTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StructuredResponseTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(StructuredResponseTest.class);
    }

    /**
     * Test the constructor with valid inputs.
     */
    public void testConstructor() {
        String status = "ok";
        String message = "Success";
        Object data = new Object(); // Replace with an appropriate object
        StructuredResponse response = new StructuredResponse(status, message, data);

        assertEquals(response.mStatus, status);
        assertEquals(response.mMessage, message);
        assertEquals(response.mData, data);
    }

    /**
     * Test the constructor with null status.
     */
    public void testConstructorWithNullStatus() {
        String message = "Error";
        Object data = new Object(); // Replace with an appropriate object
        StructuredResponse response = new StructuredResponse(null, message, data);

        assertEquals(response.mStatus, "invalid");
        assertEquals(response.mMessage, message);
        assertEquals(response.mData, data);
    }
}
