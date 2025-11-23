package com.flightapp.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.flightapp.entity.User;
import com.flightapp.repository.UserRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("sreenidhi@gmail.com");
        user.setPassword("password");
    }

    // ----------------------------------------------------
    // REGISTER TEST
    // ----------------------------------------------------
    @Test
    void testRegister_Success() {
        when(userRepository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(authService.register(user))
                .expectNext(user)
                .verifyComplete();
    }

    // ----------------------------------------------------
    // LOGIN SUCCESS
    // ----------------------------------------------------
    @Test
    void testLogin_Success() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(authService.login("sreenidhi@gmail.com", "password"))
                .assertNext(sessionId -> assertNotNull(sessionId))
                .verifyComplete();
    }

    // ----------------------------------------------------
    // LOGIN – USER NOT FOUND
    // ----------------------------------------------------
    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("sreenidhi@gmail.com"))
                .thenReturn(Mono.empty());

        StepVerifier.create(authService.login("sreenidhi@gmail.com", "password"))
                .expectErrorMatches(e ->
                        e instanceof RuntimeException &&
                        e.getMessage().equals("User not found"))
                .verify();
    }

    // ----------------------------------------------------
    // LOGIN – INVALID PASSWORD
    // ----------------------------------------------------
    @Test
    void testLogin_InvalidPassword() {
        User wrongUser = new User();
        wrongUser.setEmail("sreenidhi@gmail.com");
        wrongUser.setPassword("wrongpass");

        when(userRepository.findByEmail("sreenidhi@gmail.com"))
                .thenReturn(Mono.just(wrongUser));

        StepVerifier.create(authService.login("sreenidhi@gmail.com", "password"))
                .expectErrorMatches(e ->
                        e instanceof RuntimeException &&
                        e.getMessage().equals("Invalid password"))
                .verify();
    }

    // ----------------------------------------------------
    // GET LOGGED-IN USER SUCCESS
    // ----------------------------------------------------
    @Test
    void testGetLoggedInUser_Success() {

        // Mock login
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Mono.just(user));

        String sessionId = authService.login("sreenidhi@gmail.com", "password")
                .block();  // session stored internally

        // Mock again for getUser
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Mono.just(user));

        StepVerifier.create(authService.getloggedInUser(sessionId))
                .expectNext(user)
                .verifyComplete();
    }

    // ----------------------------------------------------
    // GET LOGGED-IN USER – INVALID SESSION
    // ----------------------------------------------------
    @Test
    void testGetLoggedInUser_InvalidSession() {

        StepVerifier.create(authService.getloggedInUser("bad-session-id"))
                .expectErrorMatches(e ->
                        e instanceof RuntimeException &&
                        e.getMessage().equals("Invalid session ID"))
                .verify();
    }
}