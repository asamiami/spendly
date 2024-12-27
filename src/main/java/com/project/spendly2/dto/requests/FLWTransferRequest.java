package com.project.spendly2.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FLWTransferRequest(

        @JsonProperty("account_bank")
        String accountBank,
        @JsonProperty("account_number")
        String accountNumber,

        Integer amount,

        String narration,


        String currency,

        String reference

) {
}
