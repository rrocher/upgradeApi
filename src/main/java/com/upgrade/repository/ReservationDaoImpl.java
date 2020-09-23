package com.upgrade.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

import com.upgrade.domain.Customer;
import com.upgrade.domain.Reservation;

public class ReservationDaoImpl {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	

	public int insertReservation(Reservation res) {
		String sql = "INSERT INTO reservations (identifier, startDate, endDate, customer) VALUES (?, ?, ?, ?)";
		return jdbcTemplate.update(sql, new Object[] { res.getIdentifier(), res.getStartDate(), res.getEndDate(), res.getCustomer().getId() });
	}

	public void insertReservations(List<Reservation> reservations) {
		// TODO Auto-generated method stub

	}


}
