package com.project.spendly2.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SavingsAccountRequest(

        @JsonProperty("account_name")
        String accountName,

        String email,

        @JsonProperty("mobile_number")
        String mobileNumber,

        String country
) {
}
