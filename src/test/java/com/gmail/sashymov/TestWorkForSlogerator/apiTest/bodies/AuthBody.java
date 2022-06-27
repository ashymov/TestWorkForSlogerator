package com.gmail.sashymov.TestWorkForSlogerator.apiTest.bodies;

import lombok.Data;

@Data
public class AuthBody {
    private String grant_type;
    private String username;
    private String password;
}
