package com.project.spendly2.models.entities;

import com.project.spendly2.models.enums.TransactionCategory;
import com.project.spendly2.models.enums.TransactionStatus;
import com.project.spendly2.models.enums.TransactionTime;
import com.project.spendly2.models.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")

public class Transactions extends BaseEntity implements Serializable {


    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private LocalDateTime time;

    private String beneficiaryBank;

    private String bankCode;

    private String beneficiaryName;

    private String accountNumber;

    private String narration;

    private TransactionType transactionType;

    private String reference;

    @Enumerated(EnumType.STRING)
    private TransactionCategory transactionCategory;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "transaction_user_fkey")
    )
    private Users users;

    @ManyToOne
    @JoinColumn(
            name = "savings_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "transaction_user_fkey")
    )
    private Savings savings;


}
