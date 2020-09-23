package com.upgrade.service.validation;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.upgrade.dto.ReservationDto;

@Component
public class ValidatorBroker {

	private List<Validator> validatorList = Lists.newArrayList();

	public void addValidator(Validator validator) {
		validatorList.add(validator);
	}

	public void validate(ReservationDto reservation) throws ValidationException {

		for (Validator validator : validatorList) {
			validator.execute(reservation);
		}
	}
}
