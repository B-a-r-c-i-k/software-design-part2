package com.example.software;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public record RateResponse(String pair, double rate) {
}
