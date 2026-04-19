package com.example.software;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JvmExtrasMetricsConfiguration {

	@Bean
	MeterBinder processMemoryMetrics() {
		return new ProcessMemoryMetrics();
	}
}
