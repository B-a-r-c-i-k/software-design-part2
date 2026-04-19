package com.example.software;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class ClientHttpLoggingInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger log = LoggerFactory.getLogger(ClientHttpLoggingInterceptor.class);

	private static final int MAX_BODY_LOG_CHARS = 2048;

	private final String clientId;

	public ClientHttpLoggingInterceptor(@Value("${app.client-id:client}") String clientId) {
		this.clientId = clientId;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		if (!request.getHeaders().containsKey("X-Client-Id")) {
			request.getHeaders().add("X-Client-Id", clientId);
		}
		log.info("Outgoing request: method={} uri={} headers={} body={}",
				request.getMethod(),
				request.getURI(),
				request.getHeaders(),
				truncate(bodyToString(body)));
		ClientHttpResponse response = execution.execute(request, body);
		byte[] responseBody = StreamUtils.copyToByteArray(response.getBody());
		log.info("Incoming response: status={} headers={} body={}",
				response.getStatusCode(),
				response.getHeaders(),
				truncate(new String(responseBody, StandardCharsets.UTF_8)));
		return new BufferingClientHttpResponseWrapper(response, responseBody);
	}

	private static String bodyToString(byte[] body) {
		if (body == null || body.length == 0) {
			return "";
		}
		return new String(body, StandardCharsets.UTF_8);
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

	private static final class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

		private final ClientHttpResponse delegate;
		private final byte[] body;

		BufferingClientHttpResponseWrapper(ClientHttpResponse delegate, byte[] body) {
			this.delegate = delegate;
			this.body = body;
		}

		@Override
		public HttpStatusCode getStatusCode() throws IOException {
			return delegate.getStatusCode();
		}

		@Override
		public String getStatusText() throws IOException {
			return delegate.getStatusText();
		}

		@Override
		public HttpHeaders getHeaders() {
			return delegate.getHeaders();
		}

		@Override
		public InputStream getBody() throws IOException {
			return new ByteArrayInputStream(body);
		}

		@Override
		public void close() {
			delegate.close();
		}
	}
}
