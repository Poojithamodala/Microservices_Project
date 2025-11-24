package com.flightapp.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class TicketTest {

	private static Validator validator;

	@BeforeAll
	static void setup() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void testIdField() {
		Ticket ticket = new Ticket();
		ticket.setId(null);
		assertNull(ticket.getId());
		ticket.setId("12345");
		assertEquals("12345", ticket.getId());
	}

	@Test
	void testPNRField() {
		Ticket ticket = new Ticket();
		ticket.setPnr(null);
		assertNull(ticket.getPnr());
		ticket.setPnr("");
		assertEquals("", ticket.getPnr());
		ticket.setPnr("PNR123");
		assertEquals("PNR123", ticket.getPnr());
	}

	@Test
	void testUserIdField() {
		Ticket ticket = new Ticket();
		ticket.setUserId(null);
		assertNull(ticket.getUserId());
		ticket.setUserId("");
		assertEquals("", ticket.getUserId());
		ticket.setUserId("user123");
		assertEquals("user123", ticket.getUserId());
	}

	@Test
	void testDepartureAndReturnFlightId() {
		Ticket ticket = new Ticket();
		ticket.setDepartureFlightId(null);
		assertNull(ticket.getDepartureFlightId());
		ticket.setDepartureFlightId("dep001");
		assertEquals("dep001", ticket.getDepartureFlightId());

		ticket.setReturnFlightId(null);
		assertNull(ticket.getReturnFlightId());
		ticket.setReturnFlightId("ret001");
		assertEquals("ret001", ticket.getReturnFlightId());
	}

	@Test
	void testTripType() {
		Ticket ticket = new Ticket();
		ticket.setTripType(null);
		assertNull(ticket.getTripType());
		ticket.setTripType(FlightType.ONE_WAY);
		assertEquals(FlightType.ONE_WAY, ticket.getTripType());
		ticket.setTripType(FlightType.ROUND_TRIP);
		assertEquals(FlightType.ROUND_TRIP, ticket.getTripType());
	}

	@Test
	void testSeatsBooked() {
		Ticket ticket = new Ticket();
		ticket.setSeatsBooked(null);
		assertNull(ticket.getSeatsBooked());
		ticket.setSeatsBooked("");
		assertEquals("", ticket.getSeatsBooked());
		ticket.setSeatsBooked("A1,A2,A3");
		assertEquals("A1,A2,A3", ticket.getSeatsBooked());
	}

	@Test
	void testTotalPrice() {
		Ticket ticket = new Ticket();
		ticket.setTotalPrice(null);
		assertNull(ticket.getTotalPrice());
		ticket.setTotalPrice(0.0);
		assertEquals(0.0, ticket.getTotalPrice());
		ticket.setTotalPrice(-100.0);
		assertEquals(-100.0, ticket.getTotalPrice());
		ticket.setTotalPrice(500.0);
		assertEquals(500.0, ticket.getTotalPrice());
	}

	@Test
	void testBookingTime() {
		Ticket ticket = new Ticket();
		ticket.setBookingTime(null);
		assertNull(ticket.getBookingTime());
		LocalDateTime now = LocalDateTime.now();
		ticket.setBookingTime(now);
		assertEquals(now, ticket.getBookingTime());
	}

	@Test
	void testCancelFlag() {
		Ticket ticket = new Ticket();
		assertFalse(ticket.isCancel());
		ticket.setCancel(true);
		assertTrue(ticket.isCancel());
	}

	@Test
	void testPassengersList() {
		Ticket ticket = new Ticket();
		assertNull(ticket.getPassengers());
		ticket.setPassengers(new ArrayList<>());
		assertTrue(ticket.getPassengers().isEmpty());

		List<Passenger> passengers = new ArrayList<>();
		passengers.add(new Passenger());
		ticket.setPassengers(passengers);
		assertEquals(1, ticket.getPassengers().size());
	}

	@Test
	void testDuplicateSeatsInPassengersList() {
		Passenger p1 = new Passenger();
		p1.setName("Alice");
		p1.setSeatNumber("A1");

		Passenger p2 = new Passenger();
		p2.setName("Bob");
		p2.setSeatNumber("A1");

		Ticket ticket = new Ticket();
		ticket.setPassengers(Arrays.asList(p1, p2));
		ticket.setSeatsBooked("A1,A1");

		long uniqueSeatsCount = Arrays.stream(ticket.getSeatsBooked().split(",")).distinct().count();
		assertNotEquals(ticket.getPassengers().size(), uniqueSeatsCount,
				"Duplicate seats detected! Number of unique seats should match passenger count");
	}

	@Test
	void testSeatsBookedPassengerListSizeMismatch() {
		Passenger p1 = new Passenger();
		p1.setName("Alice");
		p1.setSeatNumber("A1");

		Passenger p2 = new Passenger();
		p2.setName("Bob");
		p2.setSeatNumber("A2");

		Ticket ticket = new Ticket();
		ticket.setPassengers(Arrays.asList(p1, p2));
		ticket.setSeatsBooked("A1");

		int seatsCount = ticket.getSeatsBooked().split(",").length;
		int passengersCount = ticket.getPassengers().size();

		assertNotEquals(seatsCount, passengersCount,
				"Mismatch detected! Number of seats booked should equal number of passengers");
	}
}
