package com.example.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RatePrinterService {

	private static final Logger logger = LoggerFactory.getLogger(RatePrinterService.class);

	private final RestTemplate restTemplate = new RestTemplate();
	private final ProviderProperties providerProperties;

	public RatePrinterService(ProviderProperties providerProperties) {
		this.providerProperties = providerProperties;
	}

	@Scheduled(fixedRate = 5000)
	public void printRate() {
		String url = providerProperties.getBaseUrl() + "/api/rate";
		try {
			RateResponse response = restTemplate.getForObject(url, RateResponse.class);
			if (response != null) {
				logger.info("[{}] {} = {}",
						java.time.LocalTime.now(),
						response.pair(),
						response.rate());
			}
		} catch (Exception e) {
			logger.warn("Failed to get rate from {}: {}", url, e.getMessage());
		}
	}
}
