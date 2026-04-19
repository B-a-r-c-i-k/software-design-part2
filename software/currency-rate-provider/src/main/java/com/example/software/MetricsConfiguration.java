package com.example.software;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import io.micrometer.core.instrument.binder.MeterBinder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.observation.DefaultServerRequestObservationConvention;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.http.server.observation.ServerRequestObservationConvention;
import org.springframework.lang.NonNull;

@Configuration
public class MetricsConfiguration {

	@Bean
	ServerRequestObservationConvention clientIdServerObservationConvention() {
		return new DefaultServerRequestObservationConvention() {
			@Override
			@NonNull
			public KeyValues getLowCardinalityKeyValues(@NonNull ServerRequestObservationContext context) {
				KeyValues keyValues = super.getLowCardinalityKeyValues(context);
				String clientId = "unknown";
				Object carrier = context.getCarrier();
				if (carrier instanceof HttpServletRequest request) {
					String h = request.getHeader("X-Client-Id");
					if (h != null && !h.isBlank()) {
						clientId = h;
					}
				}
				return keyValues.and(KeyValue.of("client", clientId));
			}
		};
	}

	@Bean
	MeterBinder processMemoryMetrics() {
		return new ProcessMemoryMetrics();
	}
}
