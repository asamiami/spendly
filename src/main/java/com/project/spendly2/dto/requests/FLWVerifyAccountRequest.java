package com.project.spendly2.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FLWVerifyAccountRequest(
        @JsonProperty("account_number")
        String accountNumber,
        @JsonProperty("account_bank")
        String bankAccount
) {
}
