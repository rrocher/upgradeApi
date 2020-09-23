package com.upgrade.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Rimte Rocher
 */
@Configuration
@Component
@PropertySource(value = { "classpath:application.properties" })
public class ConfigurationProperties {

	@Value("${upgrade.date.pattern}")
	private String datePattern;

	@Value("${upgrade.max.months}")
	private Integer numberOfMonths;

	@Value("${upgrade.max.days}")
	private Integer maxDays;

	@Value("${upgrade.date.presentation.pattern}")
	private String datePresentationPattern;

	@Value("${upgrade.msg.unavailable.dates}")
	private String msgUnavailableDates;

	@Value("${upgrade.msg.overlapping.dates}")
	private String msgOverlappingDates;

	@Value("${upgrade.msg.past.start}")
	private String msgPastStart;

	@Value("${upgrade.msg.maxdays.reach}")
	private String msgMaxDaysReach;

	@Value("${upgrade.msg.unallowed.future}")
	private String msgUnallowedFuture;

	@Value("${upgrade.msg.invalid.span}")
	private String msgInvalidSpan;

	public String getDatePattern() {
		return datePattern;
	}

	public Integer getNumberOfMonths() {
		return numberOfMonths;
	}

	public String getDatePresentationPattern() {
		return datePresentationPattern;
	}

	public String getMsgUnavailableDates() {
		return msgUnavailableDates;
	}

	public String getMsgOverlappingDates() {
		return msgOverlappingDates;
	}

	public String getMsgPastStart() {
		return msgPastStart;
	}

	public String getMsgMaxDaysReach() {
		return msgMaxDaysReach;
	}

	public String getMsgUnallowedFuture() {
		return msgUnallowedFuture;
	}

	public String getMsgInvalidSpan() {
		return msgInvalidSpan;
	}

	public Integer getMaxDays() {
		return maxDays;
	}

}
