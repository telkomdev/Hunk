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

        Hunk.HttpMethod httpMethodGet0 = Hunk.HttpMethod.from("get");
        Hunk.HttpMethod httpMethodGet1 = Hunk.HttpMethod.from("GET");

        assertEquals(Hunk.HttpMethod.GET, httpMethodGet0);
        assertEquals(Hunk.HttpMethod.GET, httpMethodGet1);

        Hunk.HttpMethod httpMethodPost0 = Hunk.HttpMethod.from("post");
        Hunk.HttpMethod httpMethodPost1 = Hunk.HttpMethod.from("POST");

        assertEquals(Hunk.HttpMethod.POST, httpMethodPost0);
        assertEquals(Hunk.HttpMethod.POST, httpMethodPost1);

        Hunk.HttpMethod httpMethodPut0 = Hunk.HttpMethod.from("put");
        Hunk.HttpMethod httpMethodPut1 = Hunk.HttpMethod.from("PUT");

        assertEquals(Hunk.HttpMethod.PUT, httpMethodPut0);
        assertEquals(Hunk.HttpMethod.PUT, httpMethodPut1);

        Hunk.HttpMethod httpMethodInvalid = Hunk.HttpMethod.from("invalidHttpMethod");

        assertEquals(Hunk.HttpMethod.GET, httpMethodInvalid);
    }
}
