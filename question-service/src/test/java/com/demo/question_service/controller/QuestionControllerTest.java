package com.demo.question_service.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.question_service.model.Question;
import com.demo.question_service.service.QuestionService;

public class QuestionControllerTest {
	@Autowired
	MockMvc mockMvc;
	QuestionService questionService;
	@Test
	void getAllQuestionsTestSuccess() throws Exception{
		Question q=new Question();
		q.setCategory("ComputerScience");
		q.setId(1);
		Question q1=new Question();
		q.setCategory("Geography");
		q.setId(2);
		List<Question> li=List.of(q,q1);
		Mockito.when(questionService.getAllQuestions()).thenReturn(ResponseEntity.ok(li));
	        mockMvc.perform(get("/question/allQuestions"))
	               .andExpect(status().isOk());
	}
	@Test
	void getAllQuestionsTestFailure() throws Exception{
		Question q=new Question();
		Mockito.when(questionService.getAllQuestions()).thenReturn(ResponseEntity.ok(List.of(q)));
		
			mockMvc.perform(get("/question/allQuestions")).andExpect(status().isOk()).equals(q);
	    
	}
	@Test
	void getQuestionsByCategoryTestSuccess() throws Exception{
		Question q=new Question();
		q.setCategory("ComputerScience");
		q.setId(1);
		Mockito.when(questionService.getQuestionsByCategory("ComputerScience")).thenReturn(ResponseEntity.ok(List.of(q)));
		mockMvc.perform(get("/question/category/{category}","ComputerScience")).andExpect(status().isOk());
        
	}
	@Test
	void getQuestionsByCategoryTestFailure() throws Exception{
		Mockito.when(questionService.getQuestionsByCategory("ComputerScience")).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		mockMvc.perform(get("/question/category/{category}","ComputerScience")).andExpect(status().isNotFound());
	}
//	@Test
//	void getQuestionsByIdTestSuccess() throws Exception {
//	    QuestionWrapper q = new QuestionWrapper();
//	    q.setId(1);
//	    q.setQuestionTitle("Hello");
//	    Mockito.when(questionService.getQuestionsFromId(List.of(1)))
//	           .thenReturn(ResponseEntity.ok(List.of(q)));
//
//	    mockMvc.perform(post("/question/getQuestions/{id}",List.of(1))
//	            .contentType(MediaType.APPLICATION_JSON)
//	            .content("[1]"))
//	           .andExpect(status().isOk());
//	           
//	}

	@Test
	void getQuestionsByIdTestFailure() throws Exception{
		Mockito.when(questionService.getQuestionsFromId(List.of(1))).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		mockMvc.perform(post("/question/getQuestions/{id}",List.of(1))).andExpect(status().isNotFound());
	}
	
}