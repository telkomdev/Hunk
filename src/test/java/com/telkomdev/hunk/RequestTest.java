package com.telkomdev.hunk;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RequestTest extends TestCase {

    public RequestTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    public void testHttpMethod() {
        assertTrue(true);

        Request.HttpMethod httpMethodGet0 = Request.HttpMethod.fromString("get");
        Request.HttpMethod httpMethodGet1 = Request.HttpMethod.fromString("GET");

        assertEquals(Request.HttpMethod.GET, httpMethodGet0);
        assertEquals(Request.HttpMethod.GET, httpMethodGet1);

        Request.HttpMethod httpMethodPost0 = Request.HttpMethod.fromString("post");
        Request.HttpMethod httpMethodPost1 = Request.HttpMethod.fromString("POST");

        assertEquals(Request.HttpMethod.POST, httpMethodPost0);
        assertEquals(Request.HttpMethod.POST, httpMethodPost1);

        Request.HttpMethod httpMethodPut0 = Request.HttpMethod.fromString("put");
        Request.HttpMethod httpMethodPut1 = Request.HttpMethod.fromString("PUT");

        assertEquals(Request.HttpMethod.PUT, httpMethodPut0);
        assertEquals(Request.HttpMethod.PUT, httpMethodPut1);

        Request.HttpMethod httpMethodInvalid = Request.HttpMethod.fromString("invalidHttpMethod");

        assertEquals(Request.HttpMethod.GET, httpMethodInvalid);
    }
}
