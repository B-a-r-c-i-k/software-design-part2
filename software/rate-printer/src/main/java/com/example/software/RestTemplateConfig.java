package com.example.software;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(ClientHttpLoggingInterceptor loggingInterceptor) {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		BufferingClientHttpRequestFactory bufferingFactory = new BufferingClientHttpRequestFactory(factory);
		RestTemplate template = new RestTemplate(bufferingFactory);
		template.setInterceptors(List.of(loggingInterceptor));
		return template;
	}
}
