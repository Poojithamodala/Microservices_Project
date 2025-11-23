package com.flightapp.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.flightapp.entity.Flight;
import com.flightapp.repository.FlightRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FlightService {

	private final FlightRepository flightRepository;

	public FlightService(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	public Mono<Flight> addFlight(Flight flight) {
		return flightRepository.save(flight);
	}

	public Mono<Void> deleteFlight(String flightId) {
		return flightRepository.deleteById(flightId);
	}

	public Mono<Flight> updateFlight(String id, Map<String, Object> updates) {

		return flightRepository.findById(id).switchIfEmpty(Mono.error(new RuntimeException("Flight not found")))
				.flatMap(flight -> {

					if (updates.containsKey("airline")) {
						flight.setAirline((String) updates.get("airline"));
					}
					if (updates.containsKey("fromPlace")) {
						flight.setFromPlace((String) updates.get("fromPlace"));
					}
					if (updates.containsKey("toPlace")) {
						flight.setToPlace((String) updates.get("toPlace"));
					}
					if (updates.containsKey("departureTime")) {
						flight.setDepartureTime(LocalDateTime.parse((String) updates.get("departureTime")));
					}
					if (updates.containsKey("arrivalTime")) {
						flight.setArrivalTime(LocalDateTime.parse((String) updates.get("arrivalTime")));
					}
					if (updates.containsKey("price")) {
						flight.setPrice(((Number) updates.get("price")).intValue());
					}
					if (updates.containsKey("totalSeats")) {
						flight.setTotalSeats(((Number) updates.get("totalSeats")).intValue());
					}
					if (updates.containsKey("availableSeats")) {
						flight.setAvailableSeats(((Number) updates.get("availableSeats")).intValue());
					}

					return flightRepository.save(flight);
				});
	}

	public Flux<Flight> getAllFlights() {
		return flightRepository.findAll();
	}

	public Mono<Flight> searchFlightById(String flightId) {
		return flightRepository.findById(flightId).switchIfEmpty(Mono.error(new RuntimeException("Flight not found")));
	}

	public Flux<Flight> findByFromPlaceAndToPlaceAndDepartureTimeBetween(String fromPlace, String toPlace,
			String departureTimeStr, String arrivalTimeStr) {

		LocalDateTime start = LocalDateTime.parse(departureTimeStr);
		LocalDateTime end = LocalDateTime.parse(arrivalTimeStr);

		return flightRepository.findByFromPlaceAndToPlaceAndDepartureTimeBetween(fromPlace, toPlace, start, end);
	}

	public Flux<Flight> searchFlightsByAirline(String fromPlace, String toPlace, String airline) {
		return flightRepository.findByFromPlaceAndToPlaceAndAirline(fromPlace, toPlace, airline);
	}
}
