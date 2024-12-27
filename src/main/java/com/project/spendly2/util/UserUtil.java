package com.project.spendly2.util;

import com.project.spendly2.models.entities.Users;
import com.project.spendly2.repo.UserRepository;
import com.project.spendly2.repo.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserUtil {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

        public static String getLoginUser(){

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof UserDetails){
                    return (((UserDetails) principal).getUsername());
            }else{
                return principal.toString();
            }
        }

        public  boolean isBalanceSufficient(BigDecimal amount){
            String loggedInUser = UserUtil.getLoginUser();
            Optional<Users> user = userRepository.findByEmail(loggedInUser);
            var wallet = walletRepository.findByUsers(user.get());
            return wallet.isPresent() &&
                    wallet.get().getWalletBalance().compareTo(amount) > 0;
        }



}
