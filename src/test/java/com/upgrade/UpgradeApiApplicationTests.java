package com.upgrade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.upgrade.domain.Reservation;
import com.upgrade.dto.ReservationDto;
import com.upgrade.service.ReservationService;
import com.upgrade.service.validation.ValidationException;

@SpringBootTest
class UpgradeApiApplicationTests {

	@Autowired
	ReservationService reservationSvc;

	@Test
	void contextLoads() {

	}

	private ReservationDto createReservation(int start, int duration) {
		Calendar cal = new GregorianCalendar();
		Calendar cal1 = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_WEEK, start);
		cal1.add(Calendar.DAY_OF_WEEK, start + duration);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String startDate = sdf.format(cal.getTime());
		String endDate = sdf.format(cal1.getTime());
		String firstName = "Rocher";
		String lastName = "Rimte";
		String email = "rocher.rimte@gmail.com";
		ReservationDto resDto = new ReservationDto(startDate, endDate, email, firstName, lastName);
		return resDto;

	}

	@Test
	void testSuccess() throws ParseException, ValidationException {
		ReservationDto resDto = createReservation(7, 2);
		Reservation res = reservationSvc.insertReservation(resDto);
		assertNotNull(res);
		reservationSvc.deleteReservation(resDto, res.getIdentifier());
	}

	@Test
	void testRetrievalSuccess() throws ParseException, ValidationException {
		ReservationDto resDto = createReservation(10, 2);
		Reservation res = reservationSvc.insertReservation(resDto);
		assertNotNull(res);
		Reservation res1 = reservationSvc.getReservationByIdentifier(res.getIdentifier());
		assertNotNull(res1);
		assertEquals(res1.getIdentifier(), res.getIdentifier());
		reservationSvc.deleteReservation(resDto, res.getIdentifier());
	}

	@Test
	void testModifySuccess() throws ParseException, ValidationException {
		ReservationDto resDto = createReservation(20, 2);
		Reservation res = reservationSvc.insertReservation(resDto);
		assertNotNull(res);
		resDto = createReservation(21, 2);
		res = reservationSvc.modifyReservation(resDto, res.getIdentifier());
		assertNotNull(res);
		reservationSvc.deleteReservation(resDto, res.getIdentifier());
	}

	@Test
	void testFailDuration() throws ParseException {
		Exception exception = assertThrows(ValidationException.class, () -> {

			ReservationDto resDto = createReservation(2, 4);
			Reservation res = reservationSvc.insertReservation(resDto);
			assertNotNull(res);
			reservationSvc.deleteReservation(resDto, res.getIdentifier());
		});

		String expectedMessage = "The maximum of days you can book is 3";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testFailStartDateAlreadyTaken() throws ParseException, ValidationException {

		ReservationDto resDto = createReservation(12, 2);
		Reservation res = reservationSvc.insertReservation(resDto);
		assertNotNull(res);
		Exception exception = assertThrows(ValidationException.class, () -> {
			reservationSvc.insertReservation(resDto);
		});

		String expectedMessage = "Start date is invalid, this date is already booked";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testFailOverlap() throws ParseException, ValidationException {
		ReservationDto resDto = createReservation(2, 3);
		Reservation res = reservationSvc.insertReservation(resDto);
		assertNotNull(res);
		Exception exception = assertThrows(ValidationException.class, () -> {

			ReservationDto resDto1 = createReservation(3, 3);
			reservationSvc.insertReservation(resDto1);
		});

		String expectedMessage = "Booking dates are invalid, they overlap with another booking";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
		List<String> avails = reservationSvc.getAvailabilities();
		for (int i = 0; i < avails.size(); i++) {
			System.out.println(avails.get(i));
		}
	}

	@Test
	void testDates() throws ParseException {
		String startDate = "20200928";
		String endDate = "20200930";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Date date1 = sdf.parse(startDate);

		Date date2 = sdf.parse(endDate);

		long daysBetween = ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant());
		System.out.println("daysBetween:" + daysBetween);
	}

}
