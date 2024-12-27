package com.project.spendly2.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VerifyBankAccountResponse(
        @JsonProperty("account_name")
        String accountName,

        @JsonProperty("account_number")
        String accountNumber
) {
}
