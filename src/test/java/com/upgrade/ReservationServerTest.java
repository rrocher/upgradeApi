package com.upgrade;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.upgrade.domain.Reservation;
import com.upgrade.dto.ReservationDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationServerTest extends BaseTestServer {

	@BeforeEach
	public void before() {
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	public void test1() {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/v1/bookings/availabilities"),
				HttpMethod.GET, entity, String.class);
		assertEquals(response.getStatusCodeValue(), 200);

	}

	@Test
	public void testApiInsertion() throws InterruptedException, JsonMappingException, JsonProcessingException {

		ReservationDto resDto = UpgradeApiApplicationTests.createReservation(20, 2);

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<ReservationDto> entity = new HttpEntity<ReservationDto>(resDto, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/v1/bookings"), HttpMethod.POST,
				entity, String.class);

		String json = response.getBody();

		assertNotNull(json);

		Reservation reserv = new ObjectMapper().readerFor(Reservation.class).readValue(json);

		assertNotNull(reserv);
		headers.setIfMatch(String.valueOf(reserv.getVersion()));
		entity = new HttpEntity<ReservationDto>(resDto, headers);
		ResponseEntity<ReservationDto> deleteResponse = restTemplate.exchange(
				createURLWithPort("/v1/bookings/" + reserv.getIdentifier()), HttpMethod.DELETE, entity,
				ReservationDto.class);

		assertNotNull(deleteResponse);
		assertEquals(deleteResponse.getStatusCode(), HttpStatus.OK);

	}

	@Test
	public void testApiModification() throws InterruptedException, JsonMappingException, JsonProcessingException {

		ReservationDto resDto = UpgradeApiApplicationTests.createReservation(20, 2);

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<ReservationDto> entity = new HttpEntity<ReservationDto>(resDto, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/v1/bookings"), HttpMethod.POST,
				entity, String.class);

		String json = response.getBody();

		assertNotNull(json);

		Reservation reserv = new ObjectMapper().readerFor(Reservation.class).readValue(json);

		assertNotNull(reserv);
		resDto = UpgradeApiApplicationTests.createReservation(21, 2);
		headers.setIfMatch(response.getHeaders().getETag());
		entity = new HttpEntity<ReservationDto>(resDto, headers);
		ResponseEntity<ReservationDto> updateResponse = restTemplate.exchange(
				createURLWithPort("/v1/bookings/" + reserv.getIdentifier()), HttpMethod.PUT, entity,
				ReservationDto.class);

		assertNotNull(updateResponse);

		assertEquals(updateResponse.getStatusCode(), HttpStatus.OK);

		headers.setIfMatch(updateResponse.getHeaders().getETag());
		entity = new HttpEntity<ReservationDto>(resDto, headers);
		ResponseEntity<ReservationDto> deleteResponse = restTemplate.exchange(
				createURLWithPort("/v1/bookings/" + reserv.getIdentifier()), HttpMethod.DELETE, entity,
				ReservationDto.class);

		assertNotNull(deleteResponse);
		assertEquals(deleteResponse.getStatusCode(), HttpStatus.OK);
	}

	@SuppressWarnings("null")
	@Test
	public void testApiConcurrentModification()
			throws InterruptedException, JsonMappingException, JsonProcessingException {

		ReservationDto resDto = UpgradeApiApplicationTests.createReservation(24, 3);

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

		HttpEntity<ReservationDto> entity = new HttpEntity<ReservationDto>(resDto, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/v1/bookings"), HttpMethod.POST,
				entity, String.class);

		String json = response.getBody();

		assertNotNull(json);

		Reservation reserv = new ObjectMapper().readerFor(Reservation.class).readValue(json);

		assertNotNull(reserv);

		resDto = UpgradeApiApplicationTests.createReservation(24, 2);
		headers.setIfMatch(response.getHeaders().getETag());

		int numberOfThreads = 2;
		ExecutorService service = Executors.newFixedThreadPool(2);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		ArrayList<HttpStatus> status = Lists.newArrayList();
		for (int i = 0; i < numberOfThreads; i++) {
			service.execute(() -> {

				final ReservationDto resDtoTmp = UpgradeApiApplicationTests.createReservation(25, 2);
				final HttpEntity<ReservationDto> entityTmp = new HttpEntity<ReservationDto>(resDtoTmp, headers);
				final ResponseEntity<ReservationDto> tmp = restTemplate.exchange(
						createURLWithPort("/v1/bookings/" + reserv.getIdentifier()), HttpMethod.PUT, entityTmp,
						ReservationDto.class);

				latch.countDown();

				assertNotNull(tmp);

				status.add(tmp.getStatusCode());
			});
		}
		latch.await();
		assertTrue(status.stream().anyMatch(item -> HttpStatus.PRECONDITION_FAILED.equals(item)));
		
		headers.setIfMatch("1");
		entity = new HttpEntity<ReservationDto>(resDto, headers);
		ResponseEntity<ReservationDto> deleteResponse = restTemplate.exchange(
				createURLWithPort("/v1/bookings/" + reserv.getIdentifier()), HttpMethod.DELETE, entity,
				ReservationDto.class);

		assertNotNull(deleteResponse);
		assertEquals(deleteResponse.getStatusCode(), HttpStatus.OK);

	}

}
