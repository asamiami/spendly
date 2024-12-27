package com.project.spendly2.services;

import com.project.spendly2.dto.requests.SpendlyTransferRequest;
import com.project.spendly2.dto.requests.TransferRequest;
import com.project.spendly2.dto.responses.ApiResponse;
import com.project.spendly2.dto.responses.TransferResponse;
import com.project.spendly2.models.entities.Users;
import com.project.spendly2.models.entities.Wallet;


public interface WalletServices {
     Wallet createWallet(Users user);

     ApiResponse<String> spendlyToSpendlyTransfer (SpendlyTransferRequest request);

     ApiResponse<TransferResponse> transferToBank(TransferRequest request);

}
