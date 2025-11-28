package com.flightapp.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Document("tickets")
public class Ticket {

	@Id
	private String id;

	private String pnr;

	private String userId;
	private String departureFlightId;
	private String returnFlightId;

	private FlightType tripType;

	private String seatsBooked;

	private Double totalPrice;

	private LocalDateTime bookingTime;

	private boolean cancel;
	
	@Valid
	@NotEmpty(message = "At least one passenger is required")
	private List<Passenger> passengers;
}
