package com.demo.quiz_service.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.quiz_service.model.QuestionWrapper;
import com.demo.quiz_service.model.QuizDto;
import com.demo.quiz_service.service.QuizService;

class QuizControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private QuizService quizService;

	private QuizDto dto;

	@BeforeEach
	void setUp() {
		dto = new QuizDto();
		dto.setCategoryName("java");
		dto.setNumQuestions(3);
		dto.setTitle("Quiz 1");
	}

	@Test
	void testCreateQuiz() throws Exception {
		when(quizService.createQuiz("java", 3, "Quiz 1")).thenReturn(ResponseEntity.ok("Success"));

		mockMvc.perform(post("/quiz/create").contentType(MediaType.APPLICATION_JSON)
				.content("{\"categoryName\":\"java\",\"numQuestions\":3,\"title\":\"Quiz 1\"}"))
				.andExpect(status().isOk()).andExpect(content().string("Success"));
	}

	@Test
	void testGetQuizQuestions() throws Exception {
		List<QuestionWrapper> list = Arrays.asList(new QuestionWrapper(1, "Q1", "A", "B", "C", "D"));

		when(quizService.getQuizQuestions(1)).thenReturn(ResponseEntity.ok(list));

		mockMvc.perform(post("/quiz/get/1")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].questionTitle").value("Q1"));
	}

	@Test
	void testSubmitQuiz() throws Exception {
		when(quizService.calculateResult(eq(1), anyList())).thenReturn(ResponseEntity.ok(2));

		mockMvc.perform(post("/quiz/submit/1").contentType(MediaType.APPLICATION_JSON).content("[]"))
				.andExpect(status().isOk()).andExpect(content().string("2"));
	}
}