package com.project.spendly2.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FLWVerifyAccountResponse {
    private String status;
    private String message;
    private FlutterwaveResponse data;
}
