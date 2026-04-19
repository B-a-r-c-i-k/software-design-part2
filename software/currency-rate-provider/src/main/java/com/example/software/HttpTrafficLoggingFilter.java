package com.example.software;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Order(Integer.MAX_VALUE)
public class HttpTrafficLoggingFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(HttpTrafficLoggingFilter.class);

	private static final int MAX_BODY_LOG_CHARS = 2048;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (isAsyncDispatch(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
		try {
			filterChain.doFilter(requestWrapper, responseWrapper);
		} finally {
			logRequest(requestWrapper);
			logResponse(responseWrapper);
			responseWrapper.copyBodyToResponse();
		}
	}

	private static void logRequest(ContentCachingRequestWrapper request) {
		String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
		Map<String, String> safeHeaders = new LinkedHashMap<>();
		Collections.list(request.getHeaderNames()).forEach(n -> {
			if ("Authorization".equalsIgnoreCase(n) || "Cookie".equalsIgnoreCase(n)) {
				safeHeaders.put(n, "<redacted>");
			} else {
				safeHeaders.put(n, request.getHeader(n));
			}
		});
		log.info("Incoming request: method={} uri={} query={} headers={} body={}",
				request.getMethod(),
				request.getRequestURI(),
				request.getQueryString(),
				safeHeaders,
				truncate(body));
	}

	private static void logResponse(ContentCachingResponseWrapper response) {
		String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
		log.info("Outgoing response: status={} headers={} body={}",
				response.getStatus(),
				response.getHeaderNames(),
				truncate(body));
	}

	private static String truncate(String value) {
		if (value == null || value.isEmpty()) {
			return value;
		}
		if (value.length() <= MAX_BODY_LOG_CHARS) {
			return value;
		}
		return value.substring(0, MAX_BODY_LOG_CHARS) + "…(truncated)";
	}
}
