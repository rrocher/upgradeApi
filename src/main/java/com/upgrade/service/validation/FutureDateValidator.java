package com.upgrade.service.validation;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.upgrade.config.ConfigurationProperties;
import com.upgrade.dto.ReservationDto;

@Component
public class FutureDateValidator implements Validator {
	
	@Autowired
	ConfigurationProperties config;

	@Override
	public void execute(ReservationDto reservation) throws ValidationException {
		Date startDate = reservation.getParsedStartDate();
		Calendar cal = new GregorianCalendar();

		cal.add(Calendar.MONTH, config.getNumberOfMonths());

		Date oneMonthLater = cal.getTime();
		
		Date endDate = reservation.getParsedEndDate();
		if (startDate.before(new Date())) {
			throw new ValidationException(config.getMsgPastStart());
		}
		
		if (oneMonthLater.before(startDate)) {
			throw new ValidationException(config.getMsgUnallowedFuture());
		}

		if (endDate.before(startDate)) {
			throw new ValidationException(config.getMsgInvalidSpan());
		}

	}

}
