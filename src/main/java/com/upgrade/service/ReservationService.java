package com.upgrade.service;

import java.text.ParseException;
import java.util.List;

import com.upgrade.domain.Reservation;
import com.upgrade.dto.ReservationDto;
import com.upgrade.service.validation.ValidationException;
import com.upgrade.utils.NotFoundException;
/**
 * @author Rimte Rocher
 */
public interface ReservationService {
	List<ReservationDto> getReservations();

	Reservation getReservationByIdentifier(String identifier) throws NotFoundException;

	List<Reservation> getReservationByCustomer(ReservationDto res) throws NotFoundException;

	Reservation insertReservation(ReservationDto res) throws ValidationException;

	public List<String> getAvailabilities() throws ParseException;

	Reservation modifyReservation(ReservationDto res, String identifier) throws ValidationException;

	int deleteReservation(ReservationDto res, String identifier) throws NotFoundException;
}
