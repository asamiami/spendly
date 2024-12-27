package com.project.spendly2.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FlutterwaveResponse(

        @JsonProperty("response_code")
         String responseCode,
        @JsonProperty("response_message")
         String responseMessage,
        @JsonProperty("spenly_ref")
         String spendlyRef,
        @JsonProperty("account_number")
         String accountNumber,
        @JsonProperty("bank_name")
         String bankName
) {
}
