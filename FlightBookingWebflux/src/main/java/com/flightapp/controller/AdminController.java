package com.flightapp.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flightapp.entity.Flight;
import com.flightapp.entity.User;
import com.flightapp.service.AuthService;
import com.flightapp.service.FlightService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1.0/flight/airline")
public class AdminController {

	private final AuthService authService;
	private final FlightService flightService;

	public AdminController(AuthService authService, FlightService flightService) {
		this.authService = authService;
		this.flightService = flightService;
	}

	@PostMapping("/admin/login")
	public Mono<String> adminLogin(@RequestBody User user) {
		return authService.login(user.getEmail(), user.getPassword())
				.map(session -> "Login successful, session = " + session)
				.onErrorResume(e -> Mono.just("Invalid credentials"));
	}

	@PostMapping("/inventory/add")
	public Mono<String> addFlight(@RequestBody Flight flight) {
		return flightService.addFlight(flight).then(Mono.just("Flight added successfully"));
	}

	@PutMapping("/inventory/update/{id}")
	public Mono<Flight> update(@PathVariable String id, @RequestBody Map<String, Object> updates) {
		return flightService.updateFlight(id, updates);
	}

	@DeleteMapping("/inventory/delete/{id}")
	public Mono<String> delete(@PathVariable String id) {
		return flightService.deleteFlight(id).then(Mono.just("Flight deleted successfully"));
	}
}
