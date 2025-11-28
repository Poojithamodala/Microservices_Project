package com.demo.question_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.demo.question_service.model.Question;
import com.demo.question_service.model.QuestionWrapper;
import com.demo.question_service.model.Response;
import com.demo.question_service.repository.QuestionRepository;

class QuestionServiceTest {

	@InjectMocks
	private QuestionService questionService;

	@Mock
	private QuestionRepository questionDao;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	private Question sample() {
		Question q = new Question();
		q.setId(1);
		q.setQuestionTitle("What is Java?");
		q.setOption1("Language");
		q.setOption2("OS");
		q.setOption3("Food");
		q.setOption4("Game");
		q.setRightAnswer("Language");
		q.setDifficultylevel("Easy");
		q.setCategory("Programming");
		return q;
	}

	@Test
	void testGetAllQuestions() {
		List<Question> list = Arrays.asList(sample());
		when(questionDao.findAll()).thenReturn(list);

		ResponseEntity<List<Question>> response = questionService.getAllQuestions();

		assertEquals(200, response.getStatusCode().value());
		assertEquals(1, response.getBody().size());
	}

	@Test
	void testGetQuestionsByCategory() {
		when(questionDao.findByCategory("Programming")).thenReturn(Arrays.asList(sample()));

		ResponseEntity<List<Question>> response = questionService.getQuestionsByCategory("Programming");

		assertEquals(200, response.getStatusCode().value());
		assertEquals(1, response.getBody().size());
	}

	@Test
	void testAddQuestion() {
		Question q = sample();
		when(questionDao.save(q)).thenReturn(q);

		ResponseEntity<String> response = questionService.addQuestion(q);

		assertEquals(201, response.getStatusCode().value());
		assertEquals("success", response.getBody());
	}

	@Test
	void testGetQuestionsForQuiz() {
		Question q = sample();
		Pageable pageable = PageRequest.of(0, 1);

		when(questionDao.findRandomQuestionsByCategory("Programming", pageable)).thenReturn(List.of(q));

		List<Integer> ids = questionService.getQuestionsForQuiz("Programming", 1);

		assertEquals(1, ids.size());
		assertEquals(1, ids.get(0));
	}

	@Test
	void testGetQuestionsFromId() {
		Question q = sample();
		when(questionDao.findById(1)).thenReturn(Optional.of(q));

		List<Integer> ids = Arrays.asList(1);

		ResponseEntity<List<QuestionWrapper>> response = questionService.getQuestionsFromId(ids);

		assertEquals(200, response.getStatusCode().value());
		assertEquals(1, response.getBody().size());
		assertEquals("What is Java?", response.getBody().get(0).getQuestionTitle());
	}

	@Test
	void testGetScore() {
		Question q = sample();
		Response r = new Response();
		r.setId(1);
		r.setResponse("Language");

		when(questionDao.findById(1)).thenReturn(Optional.of(q));

		List<Response> responses = Arrays.asList(r);
		ResponseEntity<Integer> result = questionService.getScore(responses);

		assertEquals(200, result.getStatusCode().value());
		assertEquals(1, result.getBody());
	}
}
