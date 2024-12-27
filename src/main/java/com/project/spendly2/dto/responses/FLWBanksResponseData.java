package com.project.spendly2.dto.responses;

import java.util.List;

public record FLWBanksResponseData(
        String status,

        String message,

        List<AllBankData> data

) {
}
