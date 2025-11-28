package com.demo.question_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
class ResponseTest {
	 @Test
	    void testNoArgsConstructor() {
	        Response response = new Response();
	        assertNotNull(response);
	        assertNull(response.getId());
	        assertNull(response.getResponse());
	    }

	    @Test
	    void testSettersAndGetters() {
	        Response resp = new Response();
	        resp.setId(101);
	        resp.setResponse("Correct Answer");

	        assertEquals(101, resp.getId());
	        assertEquals("Correct Answer", resp.getResponse());
	    }
}
