package com.project.spendly2.dto.requests;

import java.math.BigDecimal;

public record SpendlyTransferRequest(

        String username,

        BigDecimal amount,

        String narration,

        String pin
) {
}
