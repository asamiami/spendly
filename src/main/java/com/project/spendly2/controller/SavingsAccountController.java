package com.project.spendly2.controller;

import com.project.spendly2.dto.requests.SavingsRequest;
import com.project.spendly2.dto.responses.ApiResponse;
import com.project.spendly2.services.SavingsServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spendly/savings")
public class SavingsAccountController {

    private final SavingsServices savingsServices;

    @PostMapping("/create-plan")
    public ApiResponse<String> createSavingsPlan (@RequestBody SavingsRequest savingsRequest){

        ApiResponse<String> response = savingsServices.createSavingsPlan(savingsRequest);
        return new ApiResponse<>(response.getMessage(), response.getCode());

    }
}
