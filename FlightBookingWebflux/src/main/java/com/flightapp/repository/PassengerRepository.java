package com.flightapp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.flightapp.entity.Passenger;

public interface PassengerRepository extends ReactiveCrudRepository<Passenger, String> {

}
