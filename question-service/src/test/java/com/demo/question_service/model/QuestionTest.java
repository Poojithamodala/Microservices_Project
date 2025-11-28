package com.demo.question_service.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {
	@Test
	void testQuestionSettersAndGetters() {
		Question question = new Question();

		question.setId(1);
		question.setQuestionTitle("What is Java?");
		question.setOption1("Programming Language");
		question.setOption2("Coffee");
		question.setOption3("Island");
		question.setOption4("Car");
		question.setRightAnswer("Programming Language");
		question.setDifficultylevel("Easy");
		question.setCategory("Java");

		assertEquals(1, question.getId());
		assertEquals("What is Java?", question.getQuestionTitle());
		assertEquals("Programming Language", question.getOption1());
		assertEquals("Coffee", question.getOption2());
		assertEquals("Island", question.getOption3());
		assertEquals("Car", question.getOption4());
		assertEquals("Programming Language", question.getRightAnswer());
		assertEquals("Easy", question.getDifficultylevel());
		assertEquals("Java", question.getCategory());
	}
}
