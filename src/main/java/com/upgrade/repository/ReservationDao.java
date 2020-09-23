package com.upgrade.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upgrade.domain.Customer;
import com.upgrade.domain.Reservation;

public interface ReservationDao extends JpaRepository<Reservation, String>{
	
	List<Reservation> findByStartDateGreaterThanEqualOrderByStartDateAsc(long startDate);
	
	List<Reservation> findByEndDateGreaterThanEqualOrderByEndDateAsc(long startDate);

	Reservation findByIdentifier(String identifier);

	List<Reservation> findByCustomer(Customer cus);
	
	Reservation findByStartDate(long startDate);
	
	Reservation findByEndDate(long endDate);

}
