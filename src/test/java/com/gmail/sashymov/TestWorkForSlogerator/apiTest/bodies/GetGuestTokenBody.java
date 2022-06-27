package com.gmail.sashymov.TestWorkForSlogerator.apiTest.bodies;

import lombok.Data;

@Data
public class GetGuestTokenBody {

    private String grant_type;
    private String scope;


}
