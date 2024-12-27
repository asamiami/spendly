package com.project.spendly2.controller;

import com.project.spendly2.dto.requests.SpendlyTransferRequest;
import com.project.spendly2.dto.requests.TransferRequest;
import com.project.spendly2.dto.responses.ApiResponse;
import com.project.spendly2.dto.responses.TransferResponse;
import com.project.spendly2.services.WalletServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/spendly/transfer")
@RequiredArgsConstructor
public class WalletController {

    private final WalletServices walletServices;

    @PostMapping("/spendly-transfer")
    public ApiResponse<String> spendlyTransfer(@RequestBody SpendlyTransferRequest spendlyTransferRequest){
        ApiResponse <String> response = walletServices.spendlyToSpendlyTransfer(spendlyTransferRequest);
        BigDecimal amount = spendlyTransferRequest.amount();
        System.out.println("Amount from request: " + amount);
        return new ApiResponse<>(response.getMessage(),response.getCode(), response.getData());
    }

    @PostMapping("/external-transfer")
    public ApiResponse<TransferResponse> externalTransfer(@RequestBody TransferRequest request){
        ApiResponse<TransferResponse> response = walletServices.transferToBank(request);
        return  new ApiResponse<>(response.getMessage(), response.getCode(), response.getData());
    }
}
