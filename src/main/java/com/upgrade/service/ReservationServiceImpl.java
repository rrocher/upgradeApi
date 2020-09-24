package com.upgrade.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.upgrade.domain.Customer;
import com.upgrade.domain.Reservation;
import com.upgrade.dto.ReservationDto;
import com.upgrade.repository.CustomerDao;
import com.upgrade.repository.ReservationDao;
import com.upgrade.service.validation.AvailabilityValidator;
import com.upgrade.service.validation.DurationValidator;
import com.upgrade.service.validation.FutureDateValidator;
import com.upgrade.service.validation.ValidationException;
import com.upgrade.service.validation.ValidatorBroker;
import com.upgrade.utils.NotFoundException;
import com.upgrade.utils.Utils;

/**
 * @author Rimte Rocher
 */
@Service
public class ReservationServiceImpl implements ReservationService {

	ReservationDao reservationDao;

	CustomerDao customerDao;

	ValidatorBroker validatorBroker;

	@Autowired
	public ReservationServiceImpl(ReservationDao reservationDao, CustomerDao customerDao,
			ValidatorBroker validatorBroker, FutureDateValidator futureDateValidator,
			DurationValidator durationValidator, AvailabilityValidator availabilityValidator) {
		super();
		this.reservationDao = reservationDao;
		this.customerDao = customerDao;
		this.validatorBroker = validatorBroker;
		this.validatorBroker.addValidator(futureDateValidator);
		this.validatorBroker.addValidator(durationValidator);
		this.validatorBroker.addValidator(availabilityValidator);

	}

	@Override
	public List<ReservationDto> getReservations() {
		return null;
	}

	@Override
	public List<String> getAvailabilities() throws ParseException {
		Calendar cal = new GregorianCalendar();
		Calendar cal1 = new GregorianCalendar();
		cal.add(Calendar.MONTH, 1);
		cal1.add(Calendar.DAY_OF_WEEK, 1);
		List<Integer> possibleDates = new ArrayList<>();

		while (!cal1.equals(cal)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String date = sdf.format(cal1.getTime());

			possibleDates.add(Integer.parseInt(date));
			cal1.add(Calendar.DAY_OF_WEEK, 1);
		}

		Collections.sort(possibleDates);

		ListIterator<Integer> iter = possibleDates.listIterator();

		while (iter.hasNext()) {
			long date = iter.next();
			Reservation reservation = reservationDao.findByStartDate(date);
			if (reservation == null) {
				continue;
			}

			iter.remove();
			long endDate = reservation.getEndDate();
			ListIterator<Integer> iter1 = iter;
			while (iter1.hasNext()) {
				long tmp = iter1.next();
				if (tmp == endDate) {
					break;
				}
				iter1.remove();
			}
		}

		String pattern = "yyyyMMdd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		List<String> availabilites = Lists.newArrayList();
		for (int i = 0; i < possibleDates.size(); i++) {
			String s = String.valueOf(possibleDates.get(i));

			Date date = simpleDateFormat.parse(s);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			availabilites.add(sdf.format(date));
		}
		return availabilites;
	}

	@Override
	@Transactional
	public Reservation insertReservation(ReservationDto res) throws ValidationException {

		this.validatorBroker.validate(res);

		Customer cust = customerDao.findByEmail(res.getEmail());

		if (cust == null) {
			cust = customerDao.save(new Customer(res.getEmail(), res.getFirstName(), res.getLastName()));
		}

		long startDate = Long.parseLong(res.getStartDate());
		long endDate = Long.parseLong(res.getEndDate());
		Reservation reservation = new Reservation(Utils.generateRandomString(), startDate, endDate, cust);
		return reservationDao.save(reservation);
	}

	@Override
	@Transactional
	public Reservation modifyReservation(ReservationDto res, String identifier) throws ValidationException {
		Reservation reservation = this.reservationDao.findByIdentifier(identifier);
		if (reservation == null) {
			throw new NotFoundException();
		}
		Reservation copy = new Reservation(reservation);

		int rc = this.deleteReservation(res, identifier);

		try {
			this.validatorBroker.validate(res);
		} catch (ValidationException e) {
			reservationDao.save(copy);
			throw e;
		}

		Customer cust = customerDao.findByEmail(res.getEmail());

		if (cust == null) {
			throw new NotFoundException();
		}

		long startDate = Long.parseLong(res.getStartDate());
		long endDate = Long.parseLong(res.getEndDate());
		Reservation reserv = new Reservation(copy.getIdentifier(), startDate, endDate, cust);
		return reservationDao.save(reserv);
	}

	@Override
	public int deleteReservation(ReservationDto res, String identifier) throws NotFoundException {
		Reservation reservation = this.reservationDao.findByIdentifier(identifier);
		if (reservation == null) {
			throw new NotFoundException();
		}

		Customer cust = customerDao.findByEmail(res.getEmail());

		if (cust == null) {
			throw new NotFoundException();
		}
		
		if (!reservation.getCustomer().equals(cust)) {

			throw new NotFoundException();
		}
		this.reservationDao.delete(reservation);
		return 0;
	}

	@Override
	public Reservation getReservationByIdentifier(String identifier) {
		Reservation reservation = this.reservationDao.findByIdentifier(identifier);
		return reservation;
	}

	@Override
	public List<Reservation> getReservationByCustomer(ReservationDto res) {
		Customer cust = customerDao.findByEmail(res.getEmail());

		if (cust == null) {
			throw new NotFoundException();
		}
		return this.reservationDao.findByCustomer(cust);
	}

}
