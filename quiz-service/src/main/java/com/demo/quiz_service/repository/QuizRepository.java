package com.demo.quiz_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.quiz_service.model.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {

}
