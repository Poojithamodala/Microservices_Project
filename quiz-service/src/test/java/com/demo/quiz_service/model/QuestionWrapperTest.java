package com.demo.quiz_service.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class QuestionWrapperTest {

	@Test
	void testConstructorAndGetters() {
		QuestionWrapper wrapper = new QuestionWrapper(1, "What is Java?", "Lang", "OS", "DB", "App");

		assertEquals(1, wrapper.getId());
		assertEquals("What is Java?", wrapper.getQuestionTitle());
		assertEquals("Lang", wrapper.getOption1());
		assertEquals("OS", wrapper.getOption2());
		assertEquals("DB", wrapper.getOption3());
		assertEquals("App", wrapper.getOption4());
	}

	@Test
	void testSetters() {
		QuestionWrapper wrapper = new QuestionWrapper(1, "Q", "A", "B", "C", "D");

		wrapper.setId(10);
		wrapper.setQuestionTitle("New Question");
		wrapper.setOption1("X1");
		wrapper.setOption2("X2");
		wrapper.setOption3("X3");
		wrapper.setOption4("X4");

		assertEquals(10, wrapper.getId());
		assertEquals("New Question", wrapper.getQuestionTitle());
		assertEquals("X1", wrapper.getOption1());
		assertEquals("X2", wrapper.getOption2());
		assertEquals("X3", wrapper.getOption3());
		assertEquals("X4", wrapper.getOption4());
	}
}