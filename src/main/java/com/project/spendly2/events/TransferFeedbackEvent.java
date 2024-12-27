package com.project.spendly2.events;

import com.project.spendly2.models.entities.Transactions;
import com.project.spendly2.models.entities.Users;
import com.project.spendly2.models.entities.Wallet;
import com.project.spendly2.models.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class TransferFeedbackEvent extends ApplicationEvent {
    private Users users;
    private Transactions transactions;


    public TransferFeedbackEvent( Users users, Transactions transactions, boolean isDebit) {
        super(users);
        this.users = users;
        this.transactions = transactions;

    }
}
