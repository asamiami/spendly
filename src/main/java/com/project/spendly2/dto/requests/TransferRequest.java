package com.project.spendly2.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransferRequest(


        String bankCode,
        @JsonProperty("account_number")
        String accountNumber,
        String receiverName,
        String bankName,
        BigDecimal amount,
        String narration,
        LocalDateTime time,
        String pin


) {
}
