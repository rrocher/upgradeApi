package com.upgrade.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;

import com.upgrade.domain.Reservation;
import com.upgrade.dto.ReservationDto;

public class Utils {
	
	public static String generateRandomString() {
	    return RandomStringUtils.randomAlphabetic(10);
	}

	public static List<ReservationDto> convertListToDto(List<Reservation> reservations ){
		Validate.notNull(reservations);
		List<ReservationDto> collect = reservations.stream().map(x -> new ReservationDto(x)).collect(Collectors.toList());
		return collect;
		
	}
}
