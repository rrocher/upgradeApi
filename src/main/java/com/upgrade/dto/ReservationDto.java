package com.upgrade.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.upgrade.domain.Reservation;

import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * @author Rimte Rocher
 */
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservationDto {

//	@JsonProperty("identifier")
	private String identifier;

//	@JsonProperty("start_date")
	private String startDate;

//	@JsonProperty("end_date")
	private String endDate;

//	@JsonProperty("email")
	private String email;

//	@JsonProperty("first_name")
	private String firstName;

//	@JsonProperty("last_name")
	private String lastName;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	private Date parseDate(String dateToParse) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date;
		try {
			date = sdf.parse(dateToParse);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Can not parse the start date of your booking");
		}
		return date;
	}

	public Date getParsedStartDate() {
		return parseDate(this.startDate);
	}

	public Date getParsedEndDate() {
		return parseDate(this.endDate);
	}

	@JsonCreator
	public ReservationDto(@JsonProperty("start_date") String startDate, @JsonProperty("end_date") String endDate,
			@JsonProperty("email") String email, @JsonProperty("first_name") String firstName,
			@JsonProperty("last_name") String lastName) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public ReservationDto(Reservation reservation) {
		this.email = reservation.getCustomer().getEmail();
		this.startDate = String.valueOf(reservation.getStartDate());
		this.endDate = String.valueOf(reservation.getEndDate());
		this.lastName = reservation.getCustomer().getLastName();
		this.firstName = reservation.getCustomer().getFirstName();
		this.identifier = reservation.getIdentifier();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public ReservationDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}
