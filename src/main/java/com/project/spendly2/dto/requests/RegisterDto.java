package com.project.spendly2.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record RegisterDto(

        @JsonProperty("firstname")
        String firstName,

        @JsonProperty("lastname")
        String lastName,

        @JsonProperty("email")
        String email,

        String nickname,

        String password,

        String bvn,

        String phonenumber,

        String transactionPin


) {
}
