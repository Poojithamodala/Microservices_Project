package com.flightapp.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.flightapp.entity.Flight;
import com.flightapp.entity.FlightType;
import com.flightapp.entity.Passenger;
import com.flightapp.entity.Ticket;
import com.flightapp.repository.FlightRepository;
import com.flightapp.repository.TicketRepository;
import com.flightapp.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TicketService {

	private final FlightRepository flightRepository;
	private final UserRepository userRepository;
	private final TicketRepository ticketRepository;

	public TicketService(FlightRepository flightRepository, UserRepository userRepository,
			TicketRepository ticketRepository) {
		this.flightRepository = flightRepository;
		this.userRepository = userRepository;
		this.ticketRepository = ticketRepository;
	}

	public Mono<String> bookTicket(String userId, String departureFlightId, String returnFlightId,
			List<Passenger> passengers, FlightType tripType) {

		int seatCount = passengers.size();
		List<String> seatList = passengers.stream().map(Passenger::getSeatNumber).collect(Collectors.toList());

		Set<String> uniqueSeats = new HashSet<>(seatList);
		if (uniqueSeats.size() != seatList.size()) {
			return Mono.error(new RuntimeException("Duplicate seat numbers selected"));
		}

		Mono<Void> seatAlreadyBookedCheck = ticketRepository.findByDepartureFlightId(departureFlightId)
				.flatMap(existing -> {
					List<String> booked = Arrays.asList(existing.getSeatsBooked().split(","));
					for (String seat : seatList) {
						if (booked.contains(seat)) {
							return Mono.error(new RuntimeException("Seat " + seat + " is already booked"));
						}
					}
					return Mono.empty();
				}).then();

		return seatAlreadyBookedCheck
				.then(userRepository.findById(userId).switchIfEmpty(Mono.error(new RuntimeException("User not found")))
						.flatMap(user -> flightRepository.findById(departureFlightId)
								.switchIfEmpty(Mono.error(new RuntimeException("Departure flight not found")))
								.flatMap(depFlight -> {

									if (depFlight.getAvailableSeats() < seatCount)
										return Mono.error(new RuntimeException("Not enough seats in departure flight"));

									depFlight.setAvailableSeats(depFlight.getAvailableSeats() - seatCount);

									return flightRepository.save(depFlight).flatMap(savedDep -> {

										if (tripType == FlightType.ONE_WAY) {
											Ticket t = createTicket(userId, savedDep, null, passengers, tripType);
											return ticketRepository.save(t).map(Ticket::getPnr);
										}

										return flightRepository.findById(returnFlightId)
												.switchIfEmpty(
														Mono.error(new RuntimeException("Return flight not found")))
												.flatMap(retFlight -> {

													if (retFlight.getAvailableSeats() < seatCount)
														return Mono.error(new RuntimeException(
																"Not enough seats in return flight"));

													retFlight.setAvailableSeats(
															retFlight.getAvailableSeats() - seatCount);

													return flightRepository.save(retFlight).flatMap(savedRet -> {
														Ticket t = createTicket(userId, savedDep, savedRet, passengers,
																tripType);
														return ticketRepository.save(t).map(Ticket::getPnr);
													});
												});
									});
								})));
	}

	private Ticket createTicket(String userId, Flight depFlight, Flight retFlight, List<Passenger> passengers,
			FlightType tripType) {

		Ticket ticket = new Ticket();
		ticket.setUserId(userId);
		ticket.setDepartureFlightId(depFlight.getId());
		ticket.setReturnFlightId(retFlight != null ? retFlight.getId() : null);
		ticket.setTripType(tripType);
		ticket.setPnr(UUID.randomUUID().toString().substring(0, 8));
		ticket.setBookingTime(LocalDateTime.now());

		// Concatenate seats
		String seats = passengers.stream().map(Passenger::getSeatNumber).collect(Collectors.joining(","));
		ticket.setSeatsBooked(seats);

		int seatCount = passengers.size();
		double total = depFlight.getPrice() * seatCount;
		if (retFlight != null)
			total += retFlight.getPrice() * seatCount;

		ticket.setTotalPrice(total);

		return ticket;
	}

	public Flux<Ticket> getHistory(String email) {
		return userRepository.findByEmail(email).switchIfEmpty(Mono.error(new RuntimeException("User not found")))
				.flatMapMany(user -> ticketRepository.findByUserId(user.getId()));
	}

	public Mono<Ticket> getTicketByPnr(String pnr) {
		return ticketRepository.findByPnr(pnr).switchIfEmpty(Mono.error(new RuntimeException("No ticket found")));
	}

	public Mono<String> cancelTicket(String pnr) {
		return ticketRepository.findByPnr(pnr).switchIfEmpty(Mono.error(new RuntimeException("PNR not found")))
				.flatMap(ticket -> {

					if (ticket.isCancel()) {
						return Mono.just("Ticket already cancelled");
					}

					return flightRepository.findById(ticket.getDepartureFlightId())
							.switchIfEmpty(Mono.error(new RuntimeException("Departure flight not found")))
							.flatMap(depFlight -> {

								LocalDateTime now = LocalDateTime.now();
								if (depFlight.getDepartureTime().isBefore(now.plusHours(24))) {
									return Mono.error(new RuntimeException(
											"Cannot cancel ticket less than 24 hours before departure"));
								}
								int seatCount = (ticket.getSeatsBooked() != null && !ticket.getSeatsBooked().isEmpty())
										? ticket.getSeatsBooked().split(",").length
										: 1;

								Mono<Flight> saveDep = flightRepository.findById(ticket.getDepartureFlightId())
										.flatMap(dep -> {
											dep.setAvailableSeats(dep.getAvailableSeats() + seatCount);
											return flightRepository.save(dep);
										});

								Mono<Flight> saveRet = Mono.empty();
								if (ticket.getReturnFlightId() != null) {
									saveRet = flightRepository.findById(ticket.getReturnFlightId()).flatMap(ret -> {
										ret.setAvailableSeats(ret.getAvailableSeats() + seatCount);
										return flightRepository.save(ret);
									});
								}

								ticket.setCancel(true);
								Mono<Ticket> saveTicket = ticketRepository.save(ticket);

								return Mono.when(saveDep, saveRet, saveTicket)
										.then(Mono.just("Cancelled Successfully"));
							});
				});
	}
}
