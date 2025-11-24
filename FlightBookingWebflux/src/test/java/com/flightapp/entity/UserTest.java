package com.flightapp.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class UserTest {

	private static Validator validator;

	@BeforeAll
	static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void idShouldBeNullBeforeSaving() {
		User user = new User();
		assertNull(user.getId());
	}

	@Test
	void idShouldPersistIfManuallySet() {
		User user = new User();
		user.setId("Id123");
		assertEquals("Id123", user.getId());
	}

	@Test
	void nameShouldFailIfNull() {
		User user = new User();
		user.setName(null);

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
	}

	@Test
	void emailShouldFailIfNull() {
		User user = new User();
		user.setEmail(null);

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
	}

	@Test
	void ageShouldFailIfNull() {
		User user = new User();
		user.setAge(null);

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("age")));
	}
	
	@Test
	void genderShouldFailIfNull() {
		User user = new User();
		user.setGender(null);

		Set<ConstraintViolation<User>> violations = validator.validate(user);

		assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("gender")));
	}
}
