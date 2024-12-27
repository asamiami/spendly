package com.project.spendly2.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Wallet extends BaseEntity{



    private BigDecimal walletBalance;

    private String WalletNumber;


    private String reference;
    private String bankName;

    @OneToOne
    private Users users;

    public void addUser(Users user){
        this.users = user;
    }

    public void  updateBalance(BigDecimal amount, boolean isDebit){
        if(isDebit){
            this.setWalletBalance(walletBalance.subtract(amount));
        }else {
            this.walletBalance = walletBalance.add(amount);
        }
    }

}
