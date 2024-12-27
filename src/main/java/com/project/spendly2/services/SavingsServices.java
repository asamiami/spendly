package com.project.spendly2.services;

import com.project.spendly2.dto.requests.SavingsRequest;
import com.project.spendly2.dto.responses.ApiResponse;
import com.project.spendly2.models.entities.SavingsAccount;
import com.project.spendly2.models.entities.Users;

public interface SavingsServices {

     ApiResponse<String> createSavingsPlan(SavingsRequest request);
     SavingsAccount createSavingsAccount(Users users);
}
