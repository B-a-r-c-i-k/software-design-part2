package com.example.software;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api")
public class RateController {

	private static final double BASE_RATE = 80.0;
	private static final double RANDOM_DELTA = 2.0;

	@GetMapping("/rate")
	public RateResponse getUsdRubRate() {
		double randomDelta = (ThreadLocalRandom.current().nextDouble() * 2 - 1) * RANDOM_DELTA;
		double rate =  BASE_RATE + randomDelta;
		return new RateResponse("USDRUB", rate);
	}
}
