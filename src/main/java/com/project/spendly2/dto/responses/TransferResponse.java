package com.project.spendly2.dto.responses;

import java.math.BigDecimal;

public record TransferResponse(

        String fullname,

        String bankCode,

        String bankName,

        String accountNumber,

        BigDecimal amount,

        String narration,

        String reference,

        String status,

        String message
) {
}
