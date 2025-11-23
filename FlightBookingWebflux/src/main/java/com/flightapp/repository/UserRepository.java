package com.flightapp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.flightapp.entity.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByEmail(String email);
}
