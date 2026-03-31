package com.example.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RatePrinterService {

	private static final Logger logger = LoggerFactory.getLogger(RatePrinterService.class);

	private static final String PROVIDER_SERVICE_ID = "currency-rate-provider";
	private static final String RATE_URL = "http://" + PROVIDER_SERVICE_ID + "/api/rate";

	private final RestTemplate restTemplate;

	public RatePrinterService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Scheduled(fixedRate = 5000)
	public void printRate() {
		try {
			RateResponse response = restTemplate.getForObject(RATE_URL, RateResponse.class);
			if (response != null) {
				logger.info("[{}] {} = {}",
						java.time.LocalTime.now(),
						response.pair(),
						response.rate());
			}
		} catch (Exception e) {
			logger.warn("Failed to get rate from {}: {}", RATE_URL, e.getMessage());
		}
	}
}
