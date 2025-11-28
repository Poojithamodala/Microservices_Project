package com.demo.question_service.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuestionWrapperTest {

	@Test
	void testSettersAndGetters() {
		QuestionWrapper wrapper = new QuestionWrapper();
		wrapper.setId(10);
		wrapper.setQuestionTitle("Title");
		wrapper.setOption1("A");
		wrapper.setOption2("B");
		wrapper.setOption3("C");
		wrapper.setOption4("D");

		assertEquals(10, wrapper.getId());
		assertEquals("Title", wrapper.getQuestionTitle());
		assertEquals("A", wrapper.getOption1());
		assertEquals("B", wrapper.getOption2());
		assertEquals("C", wrapper.getOption3());
		assertEquals("D", wrapper.getOption4());
	}

	@Test
	void testNoArgsConstructor() {
		QuestionWrapper wrapper = new QuestionWrapper();
		assertNotNull(wrapper);
		assertNull(wrapper.getId());
		assertNull(wrapper.getQuestionTitle());
		assertNull(wrapper.getOption1());
		assertNull(wrapper.getOption2());
		assertNull(wrapper.getOption3());
		assertNull(wrapper.getOption4());
	}

	@Test
	void testAllArgsConstructor() {
		QuestionWrapper wrapper = new QuestionWrapper(1, "What is Java?", "O1", "O2", "O3", "O4");

		assertEquals(1, wrapper.getId());
		assertEquals("What is Java?", wrapper.getQuestionTitle());
		assertEquals("O1", wrapper.getOption1());
		assertEquals("O2", wrapper.getOption2());
		assertEquals("O3", wrapper.getOption3());
		assertEquals("O4", wrapper.getOption4());
	}
}
