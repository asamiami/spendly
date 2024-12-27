package com.project.spendly2.models.entities;


import com.project.spendly2.models.enums.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity

@Getter
@Setter
@NoArgsConstructor
public class Savings extends BaseEntity{


    private String savingsName;

    private Long targetAmount;

    @Enumerated(EnumType.STRING)
    private Category savingsCategory;


    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "savings", cascade = CascadeType.ALL)
    private List<Transactions> transactionList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private Users users;

    @OneToOne
    private SavingsAccount savingsAccount;
    public void addTransactionToTransactionsList (Transactions transaction){

         transactionList.add(transaction);
    }
}
