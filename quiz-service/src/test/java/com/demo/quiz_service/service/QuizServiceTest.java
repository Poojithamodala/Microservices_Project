package com.demo.quiz_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.quiz_service.feign.QuizInterface;
import com.demo.quiz_service.model.QuestionWrapper;
import com.demo.quiz_service.model.Quiz;
import com.demo.quiz_service.model.Response;
import com.demo.quiz_service.repository.QuizRepository;

class QuizServiceTest {

	@Mock
	private QuizRepository quizDao;

	@Mock
	private QuizInterface quizInterface;

	@InjectMocks
	private QuizService quizService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateQuiz() {
		List<Integer> mockIds = Arrays.asList(1, 2, 3);
		when(quizInterface.getQuestionsForQuiz("java", 3)).thenReturn(new ResponseEntity<>(mockIds, HttpStatus.OK));

		Quiz savedQuiz = new Quiz();
		savedQuiz.setId(1);
		savedQuiz.setTitle("Test Quiz");
		savedQuiz.setQuestionIds(mockIds);

		when(quizDao.save(any(Quiz.class))).thenReturn(savedQuiz);

		ResponseEntity<String> response = quizService.createQuiz("java", 3, "Test Quiz");

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Success", response.getBody());
		verify(quizDao, times(1)).save(any(Quiz.class));
	}

	@Test
	void testGetQuizQuestions() {
		Quiz quiz = new Quiz();
		quiz.setId(1);
		quiz.setQuestionIds(Arrays.asList(1, 2));

		when(quizDao.findById(1)).thenReturn(Optional.of(quiz));

		List<QuestionWrapper> wrappers = Arrays.asList(new QuestionWrapper(1, "Q1", "A", "B", "C", "D"),
				new QuestionWrapper(2, "Q2", "A1", "B1", "C1", "D1"));

		when(quizInterface.getQuestionsFromId(Arrays.asList(1, 2)))
				.thenReturn(new ResponseEntity<>(wrappers, HttpStatus.OK));

		ResponseEntity<List<QuestionWrapper>> response = quizService.getQuizQuestions(1);

		assertEquals(2, response.getBody().size());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testCalculateResult() {
		List<Response> responses = new ArrayList<>();
		when(quizInterface.getScore(responses)).thenReturn(new ResponseEntity<>(2, HttpStatus.OK));

		ResponseEntity<Integer> score = quizService.calculateResult(1, responses);

		assertEquals(2, score.getBody());
		assertEquals(HttpStatus.OK, score.getStatusCode());
	}
}