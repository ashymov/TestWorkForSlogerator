package com.gmail.sashymov.TestWorkForSlogerator.apiTest.bodies;

import lombok.Data;

@Data
public class RegisterPlayerBody {
    private String username;
    private String password_change;
    private String password_repeat;
    private String email;
    private String name;
    private String surname;

}
