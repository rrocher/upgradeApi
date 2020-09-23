package com.upgrade.service.validation;

import com.upgrade.dto.ReservationDto;

public interface Validator {
	
	public void execute(ReservationDto reservation) throws ValidationException;

}
