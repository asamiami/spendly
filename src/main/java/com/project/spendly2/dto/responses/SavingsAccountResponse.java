package com.project.spendly2.dto.responses;

public record SavingsAccountResponse(

        String status,

        String message,

        FlutterwaveResponse data
) {
}
