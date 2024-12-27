package com.project.spendly2.dto.responses;

public record FLWVerifyBankAccResponse(

        String status,

        String message,

        VerifyBankAccountResponse data
){
}
