package com.project.spendly2.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WalletRequet(

        String email,

        String bvn,

        String firstname,

        String lastname,

        String phonenmuber,

        Integer amount,

        @JsonProperty("is_permanent")
        Boolean isPermanent,

        @JsonProperty("tx_ref")
        String txref
) {
}
