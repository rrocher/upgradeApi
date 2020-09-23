package com.upgrade.service.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.upgrade.config.ConfigurationProperties;
import com.upgrade.domain.Reservation;
import com.upgrade.dto.ReservationDto;
import com.upgrade.repository.ReservationDao;

@Component
public class AvailabilityValidator implements Validator {

	ReservationDao reservationDao;

	@Autowired
	ConfigurationProperties config;
	
	@Autowired
	public AvailabilityValidator(ReservationDao reservationDao) {
		super();
		this.reservationDao = reservationDao;
	}


	@Override
	public void execute(ReservationDto res) throws ValidationException {
		long startDate = Long.parseLong(res.getStartDate());
		long endDate = Long.parseLong(res.getEndDate());      
		List<Reservation> currentReservations = reservationDao
				.findByStartDateGreaterThanEqualOrderByStartDateAsc(startDate);
		
		currentReservations.addAll(reservationDao.findByEndDateGreaterThanEqualOrderByEndDateAsc(startDate));
		
		if (currentReservations.isEmpty())
			return;
		for (Reservation reservation : currentReservations) {
			if (reservation.getStartDate() == startDate) {
				throw new ValidationException(config.getMsgUnavailableDates());
			}

			if ((reservation.getStartDate() > startDate) && (reservation.getStartDate()  < endDate)) {
				throw new ValidationException(config.getMsgOverlappingDates());
			}
			if ((reservation.getEndDate() > startDate) &&  reservation.getEndDate() <= endDate) {
				throw new ValidationException(config.getMsgOverlappingDates());
			}

		}

	}

}