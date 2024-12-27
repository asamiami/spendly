package com.project.spendly2.services.implementation;

import com.project.spendly2.dto.requests.SavingsAccountRequest;
import com.project.spendly2.dto.requests.SavingsRequest;
import com.project.spendly2.dto.responses.ApiResponse;
import com.project.spendly2.models.entities.Savings;
import com.project.spendly2.models.entities.SavingsAccount;
import com.project.spendly2.models.entities.Users;
import com.project.spendly2.models.entities.Wallet;
import com.project.spendly2.repo.SavingsRepository;
import com.project.spendly2.repo.SubAccountsRepository;
import com.project.spendly2.repo.UserRepository;
import com.project.spendly2.repo.WalletRepository;
import com.project.spendly2.services.SavingsServices;
import com.project.spendly2.util.FLWUtill;
import com.project.spendly2.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SavingsAccountServImpl implements SavingsServices {

    private final SubAccountsRepository subAccountsRepository;
    private final FLWUtill flwUtill;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final SavingsRepository savingsRepository;


    public SavingsAccount createSavingsAccount(Users users){

        Optional<Wallet> wallet = walletRepository.findByUsers(users);

        SavingsAccount savings = flwUtill.createSavingsAccount(users.getFirstName() + " " + users.getLastName(), users.getEmail(), users.getPhonenumber());
        return subAccountsRepository.save(savings);
    }



    public ApiResponse<String> createSavingsPlan(SavingsRequest request){

        String loggedIn = UserUtil.getLoginUser();

        Optional<Users> users = userRepository.findByEmail(loggedIn);
        SavingsAccount savingsAccount = createSavingsAccount(users.get());
        List<Savings> saving = new ArrayList<>();

        if (users.isPresent()) {
            Savings savings = new Savings();
            saving.add(savings);
            savings.setSavingsAccount(savingsAccount);
            savings.setSavingsCategory(request.category());
            savings.setSavingsName(request.name());
            savings.setUsers(users.get());
            savings.setEndDate(request.endDate());
            savings.setTargetAmount(request.amount());
            savings.setStartDate(request.start());
            savings.setCreatedDate(LocalDateTime.now());
            users.get().setSaving(saving);
            subAccountsRepository.save(savingsAccount);
            savingsRepository.save(savings);

            return new ApiResponse<>("",HttpStatus.CREATED);
        }else{
            return  new ApiResponse<>("", HttpStatus.BAD_REQUEST);
        }
    }


}
