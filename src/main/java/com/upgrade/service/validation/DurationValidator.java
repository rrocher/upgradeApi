package com.upgrade.service.validation;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.upgrade.config.ConfigurationProperties;
import com.upgrade.dto.ReservationDto;

@Component
public class DurationValidator implements Validator {

	@Autowired
	ConfigurationProperties config;
	
	@Override
	public void execute(ReservationDto reservation) throws ValidationException {
		long daysBetween = ChronoUnit.DAYS.between(reservation.getParsedStartDate().toInstant(), 
				reservation.getParsedEndDate().toInstant());
		if (daysBetween > config.getMaxDays()) {
			throw new ValidationException(config.getMsgMaxDaysReach()+config.getMaxDays());
		}
	}

}
