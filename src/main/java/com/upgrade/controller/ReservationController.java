package com.upgrade.controller;

import java.net.URI;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.upgrade.domain.Reservation;
import com.upgrade.dto.ReservationDto;
import com.upgrade.service.ReservationService;
import com.upgrade.service.validation.ValidationException;
import com.upgrade.utils.NotFoundException;
import com.upgrade.utils.Utils;
import com.upgrade.config.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rimte Rocher
 */
@Slf4j
@RestController
@RequestMapping(value = Constants.ROOT_API, path = Constants.ROOT_API)
@Api(description = "API to provide reservation feature and information of the availability of a campsite for a given date range with the default being 1 month.")
public class ReservationController {

	@Autowired
	ReservationService reservationService;

	@GetMapping(Constants.AVAILABILITIES)
	@ApiOperation(value = "Retrieve the availabilities for a future reservation")
	public ResponseEntity<List<String>> availabilities(UriComponentsBuilder ucb) throws ParseException {
		
		List<String> availabilites = null;
		
		try {
			availabilites = reservationService.getAvailabilities();

		} catch (Exception e) {
			log.info("Exception when retrieving availabilities:[{}]", e.getMessage());
			throw new NotFoundException();
		}

		if (availabilites == null)
			throw new NotFoundException();

		HttpHeaders hdrs = new HttpHeaders();
		URI locationUri = ucb.path(Constants.AVAILABILITIES_API).build().toUri();
		hdrs.setLocation(locationUri);
		ResponseEntity<List<String>> responseEntity = new ResponseEntity<List<String>>(availabilites, hdrs,
				HttpStatus.OK);
		
		return responseEntity;
	}

	@GetMapping(Constants.CUSTOMER)
	@ApiOperation(value = "Retrieve the reservations for a given customer")
	public ResponseEntity<List<ReservationDto>> getReservationByCustomer(ReservationDto res, UriComponentsBuilder ucb)
			throws ParseException {

		Validate.notNull(res, "reservation object must not be null");
		
		log.info("getReservationByCustomer called for customer [{}}", res.getEmail());

		List<Reservation> reservations = null;

		try {
			reservations = reservationService.getReservationByCustomer(res);

		} catch (Exception e) {
			log.info("Exception when getting reservations for customer [{}]:[{}]", res.getEmail(), e.getMessage());
			throw new NotFoundException();
		}
		if (reservations == null)
			throw new NotFoundException();

		HttpHeaders hdrs = new HttpHeaders();
		URI locationUri = ucb.path(Constants.CUSTOMER_API).build().toUri();
		hdrs.setLocation(locationUri);
		ResponseEntity<List<ReservationDto>> responseEntity = new ResponseEntity<List<ReservationDto>>(
				Utils.convertListToDto(reservations), hdrs, HttpStatus.OK);
		
		return responseEntity;
	}

	@GetMapping("/{identifier}")
	@ApiOperation(value = "Retrieve the reservation for a given identifier")
	public ResponseEntity<ReservationDto> getReservation(@PathVariable("identifier") String identifier,
			UriComponentsBuilder ucb) throws ParseException {

		Validate.notNull(identifier, "identifier object must not be null");

		Reservation reservation = null;

		log.info("GetReservation called for identifier [{}}", identifier);

		try {
			reservation = reservationService.getReservationByIdentifier(identifier);

		} catch (NotFoundException e) {
			log.info("Exception when retrieving availabilities:[{}]", e.getMessage());
			throw new NotFoundException();
		}

		if (reservation == null)
			throw new NotFoundException();

		ReservationDto dto = new ReservationDto(reservation);

		HttpHeaders hdrs = new HttpHeaders();
		URI locationUri = ucb.path(Constants.ROOT_API).path("/").path(String.valueOf(dto.getIdentifier())).build()
				.toUri();
		hdrs.setLocation(locationUri);
		ResponseEntity<ReservationDto> responseEntity = new ResponseEntity<ReservationDto>(dto, hdrs, HttpStatus.OK);
		
		return responseEntity;
	}

	@RequestMapping(path = "", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
	@ApiOperation(value = "Add a reservation for a given customer after validation")
	public ResponseEntity<Reservation> addBooking(@RequestBody ReservationDto res, UriComponentsBuilder ucb)
			throws ParseException, ValidationException {
		Reservation reserv = null;


		Validate.notNull(res, "reservation object must not be null");
		
		log.info("AddBooking called for customer [{}}", res.getEmail());

		try {
			reserv = reservationService.insertReservation(res);
		} catch (ValidationException e) {
			log.info("Exception when retrieving availabilities:[{}]", e.getMessage());
			throw new ValidationException(e.getMessage());
		}

		HttpHeaders hdrs = new HttpHeaders();
		URI locationUri = ucb.path(Constants.ROOT_API).build().toUri();
		hdrs.setLocation(locationUri);
		ResponseEntity<Reservation> responseEntity = new ResponseEntity<Reservation>(reserv, hdrs, HttpStatus.CREATED);
		
		return responseEntity;
	}

	@PutMapping("/{identifier}")
	@ApiOperation(value = "Modify a reservation for a given customer after validation")
	public ResponseEntity<Reservation> modifyBooking(@PathVariable("identifier") String identifier, @RequestBody ReservationDto res,
			UriComponentsBuilder ucb) throws ParseException, ValidationException {

		Reservation reserv = null;

		Validate.notNull(res, "reservation object must not be null");
		Validate.notNull(identifier, "identifier object must not be null");

		log.info("ModifyBooking called for identifier [{}}", identifier);
		
		try {
			reserv = reservationService.modifyReservation(res, identifier);

		} catch (ValidationException e) {
			log.info("ValidationException when retrieving availabilities:[{}]", e.getMessage());
			throw new ValidationException(e.getMessage());
		}

		HttpHeaders hdrs = new HttpHeaders();
		
		URI locationUri = ucb.path(Constants.ROOT_API).path("/").path(String.valueOf(reserv.getIdentifier())).build()
				.toUri();
		hdrs.setLocation(locationUri);
		ResponseEntity<Reservation> responseEntity = new ResponseEntity<Reservation>(reserv, hdrs, HttpStatus.CREATED);
		
		return responseEntity;
	}

	@DeleteMapping("/{identifier}")
	@ApiOperation(value = "Delete a reservation for a given identifier")
	public ResponseEntity<ReservationDto> deleteBooking(@PathVariable("identifier") String identifier,
			@RequestBody ReservationDto res, UriComponentsBuilder ucb) throws ParseException, ValidationException {

		Validate.notNull(res, "reservation object must not be null");
		Validate.notNull(identifier, "identifier object must not be null");
		log.info("DeleteBooking called for identifier [{}}", identifier);

		try {
			reservationService.deleteReservation(res, identifier);

		} catch (NotFoundException e) {
			log.info("ValidationException when retrieving availabilities:[{}]", e.getMessage());
			throw new ValidationException(e.getMessage());
		}

		HttpHeaders hdrs = new HttpHeaders();
		URI locationUri = ucb.path(Constants.ROOT_API).path("/").path(String.valueOf(identifier)).build().toUri();
		hdrs.setLocation(locationUri);

		ResponseEntity<ReservationDto> responseEntity = new ResponseEntity<ReservationDto>(null, hdrs,
				HttpStatus.ACCEPTED);
		
		return responseEntity;
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error dataNotFound(NotFoundException e) {
		long id = e.getId();
		Error error;
		if (id != 0)
			error = new Error("Data Requested with id [" + id + "] not found");
		else
			error = new Error("Data Requested not found");
		error.fillInStackTrace();
		return error;
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	public Error validationException(ValidationException e) {
		Error error = new Error(e.getMessage());
		error.fillInStackTrace();
		return error;
	}

}
