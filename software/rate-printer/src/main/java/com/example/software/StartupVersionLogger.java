package com.example.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(0)
public class StartupVersionLogger implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(StartupVersionLogger.class);

	private final Optional<BuildProperties> buildProperties;

	public StartupVersionLogger(Optional<BuildProperties> buildProperties) {
		this.buildProperties = buildProperties;
	}

	@Override
	public void run(ApplicationArguments args) {
		buildProperties.ifPresentOrElse(
				bp -> log.info("Started {} version {}", bp.getName(), bp.getVersion()),
				() -> log.warn("Build properties are not available (build-info goal not run)")
		);
	}
}
