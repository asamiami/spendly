package com.project.spendly2.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SavingsAccount extends BaseEntity{

    private BigDecimal accountBalance;

    @ManyToOne
    private Users users;

    private String savingsAccount;

    private String bankName;


    public void updateSavingsBalance(BigDecimal amount, boolean isDebit) {
        if (isDebit) {
            this.accountBalance = accountBalance.subtract(amount);
        }else {
            this.accountBalance = accountBalance.add(amount);
        }
    }
}
