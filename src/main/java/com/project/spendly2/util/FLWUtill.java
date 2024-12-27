package com.project.spendly2.util;


import com.project.spendly2.dto.requests.*;
import com.project.spendly2.dto.responses.*;
import com.project.spendly2.models.entities.*;
import com.project.spendly2.models.enums.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class FLWUtill {

    protected final String FLW_SECRET_KEY = "FLWSECK_TEST-19a371c7836cd1ae0a76f79665602c41-X";

    private final RestTemplate restTemplate;

    public HttpHeaders getFlutterwaveHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + FLW_SECRET_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public Wallet createWallet(
            String emailAddress, String BVN, String txRef,
            String lastName, String firstName, String phoneNumber
    ) {
        WalletRequet body = new WalletRequet(
                emailAddress, BVN, firstName, lastName,
                phoneNumber, 40000, true, txRef
        );
        HttpEntity<WalletRequet> data = new HttpEntity<>(body, getFlutterwaveHeader());
        ResponseEntity<FLWVerifyAccountResponse> response = restTemplate.postForEntity(FlutterwaveEndpoints.VIRTUAL_ACCOUNT_NUMBER, data, FLWVerifyAccountResponse.class);

        if(response.getStatusCode().is2xxSuccessful()) {
            if(Objects.requireNonNull(response.getBody()).getStatus().equalsIgnoreCase("success")) {
                FlutterwaveResponse accountResponse = response.getBody().getData();
                if(!ObjectUtils.isEmpty(accountResponse)) {
                    Wallet wallet = new Wallet();

                    wallet.setWalletBalance(BigDecimal.valueOf(body.amount()));
                    wallet.setReference(txRef);
                    wallet.setBankName(accountResponse.bankName());
                    wallet.setWalletNumber(accountResponse.accountNumber());
                    return wallet;
                }
            }
            throw new RuntimeException("Couldn't finish processing data");
        } else {
            throw new RuntimeException("Error in creating wallet");
        }
    }

    private Transactions getTransaction(String reference, TransactionStatus status) {
        Transactions transaction = new Transactions();
        transaction.setReference(reference);
        transaction.setTransactionStatus(status);
        return transaction;
    }

    public Transactions transferMoney(TransferRequest transferDto, String ref){

        FLWTransferRequest transferRequest = new FLWTransferRequest(
                transferDto.bankCode(),
                transferDto.accountNumber(),
                transferDto.amount().intValue(),
                transferDto.narration(),
                "NGN",
                ref

        );

        HttpEntity<FLWTransferRequest> data = new HttpEntity<>(transferRequest, getFlutterwaveHeader());

        ResponseEntity<TransferResponse> response = restTemplate.postForEntity(FlutterwaveEndpoints.TRANSFER, data, TransferResponse.class);

        if (response.getStatusCode().is2xxSuccessful()){
            if (Objects.requireNonNull(response.getBody().status().equalsIgnoreCase("success"))){
                TransferResponse transferResponse = response.getBody();

                if (!ObjectUtils.isEmpty(transferResponse)){
                    log.info(transferResponse.message());
                    return getTransaction(ref, TransactionStatus.SUCCESSFUL);
                }else {
                    return getTransaction(ref, TransactionStatus.PENDING);
                }
            } else {
                log.info(response.getBody().message());
                return getTransaction(ref, TransactionStatus.FAILED);
            }
        } else {
            return getTransaction(ref, TransactionStatus.FAILED);
        }

    }

    public List<AllBankData> allBanks() {
        // Create an HTTP request with headers
        HttpEntity<Object> entity = new HttpEntity<>(getFlutterwaveHeader());

        // Make the API call
        ResponseEntity<FLWBanksResponseData> response = restTemplate.exchange(
                FlutterwaveEndpoints.GET_BANKS,
                HttpMethod.GET,
                entity,
                FLWBanksResponseData.class
        );


        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            FLWBanksResponseData data = response.getBody();


            if ("success".equalsIgnoreCase(data.status())) {
                List<AllBankData> allBanksData = data.data();


                if (!ObjectUtils.isEmpty(allBanksData)) {
                    return allBanksData;
                }
            }
        }
        return Collections.emptyList();
    }



    public ApiResponse<VerifyBankAccountResponse> verifyBankAccount(FLWVerifyAccountRequest verifyAccountDto) {
        HttpEntity<FLWVerifyAccountRequest> entity = new HttpEntity<>(verifyAccountDto, getFlutterwaveHeader());
        try {
            ResponseEntity<FLWVerifyBankAccResponse> request = restTemplate.postForEntity(
                    FlutterwaveEndpoints.VERIFY_BANK_ACCOUNT,
                    entity, FLWVerifyBankAccResponse.class
            );
            if (request.getStatusCode().is2xxSuccessful()) {
                FLWVerifyBankAccResponse body = request.getBody();
                if (Objects.requireNonNull(body).status().equalsIgnoreCase("success")) {
                    VerifyBankAccountResponse data = body.data();
                    if (!ObjectUtils.isEmpty(data)) {
                        return new ApiResponse<>("Request successfully processed", HttpStatus.OK, data);
                    }
                }
            }
            throw new RuntimeException("Error in processing request");
        } catch (Exception e) {
            throw new RuntimeException("Invalid Account. Please check your details");
        }
    }


    public SavingsAccount createSavingsAccount(String aacountName, String email, String mobileNumber) {

        SavingsAccountRequest request = new SavingsAccountRequest(aacountName, email, mobileNumber, "NG");

        HttpEntity<SavingsAccountRequest> entity = new HttpEntity<>(request, getFlutterwaveHeader());

        ResponseEntity<SavingsAccountResponse> response = restTemplate.postForEntity(FlutterwaveEndpoints.CREATE_SUBACCOUNT, entity, SavingsAccountResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            if (Objects.requireNonNull(response.getBody()).status().equalsIgnoreCase("success")) {
                FlutterwaveResponse body = response.getBody().data();
                SavingsAccount savingsAccount = new SavingsAccount();
                savingsAccount.setBankName(body.bankName());
                savingsAccount.setSavingsAccount(body.accountNumber());
                return savingsAccount;
            }
            throw new RuntimeException("Couldn't finish processing data");
        } else {
            throw new RuntimeException("Error in creating wallet");
        }
    }



}
