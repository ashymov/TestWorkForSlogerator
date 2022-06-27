package com.gmail.sashymov.TestWorkForSlogerator.apiTest.responses;

import lombok.Data;

@Data
public class AuthResponse {
    private String token_type;
    private int expires_in;
    private String access_token;
    private String refresh_token;
}
