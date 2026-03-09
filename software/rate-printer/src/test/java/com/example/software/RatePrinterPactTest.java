package com.example.software;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "currency-rate-provider")
class RatePrinterPactTest {

    @Pact(consumer = "rate-printer", provider = "currency-rate-provider")
    V4Pact usdRubRatePact(PactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody()
                .stringValue("pair", "USDRUB")
                .numberType("rate", 81.23);

        return builder
                .usingLegacyDsl()
                .given("USDRUB rate is available")
                .uponReceiving("a request for USDRUB rate")
                .path("/api/rate")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(body)
                .toPact(V4Pact.class);
    }

    @Test
    void shouldDeserializeRateResponseFromProvider(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplate();

        RateResponse response = restTemplate.getForObject(
                mockServer.getUrl() + "/api/rate",
                RateResponse.class
        );

        assertThat(response).isNotNull();
        assertThat(response.pair()).isEqualTo("USDRUB");
        assertThat(response.rate()).isGreaterThan(0.0);
    }
}

