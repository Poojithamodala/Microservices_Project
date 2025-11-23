package com.flightapp.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.flightapp.entity.User;
import com.flightapp.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class AuthService {

	private final UserRepository userRepository;

	private final Map<String, String> loginSessions = new HashMap<>();

	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Mono<User> register(User user) {
		if (user.getId() == null || user.getId().isBlank()) {
			user.setId(UUID.randomUUID().toString());
		}
		return userRepository.save(user);
	}

	public Mono<String> login(String email, String password) {
		return userRepository.findByEmail(email).switchIfEmpty(Mono.error(new RuntimeException("User not found")))
				.flatMap(user -> {
					if (!user.getPassword().equals(password)) {
						return Mono.error(new RuntimeException("Invalid password"));
					}
					String sessionId = UUID.randomUUID().toString();
					loginSessions.put(sessionId, email);
					return Mono.just(sessionId);
				});
	}

	public Mono<User> getloggedInUser(String sessionID) {
		String userEmail = loginSessions.get(sessionID);
		if (userEmail == null) {
			return Mono.error(new RuntimeException("Invalid session ID"));
		}

		return userRepository.findByEmail(userEmail).switchIfEmpty(Mono.error(new RuntimeException("User not found")));
	}
}
