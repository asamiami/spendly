package com.project.spendly2.events.listener;

import com.project.spendly2.events.TransferFeedbackEvent;
import com.project.spendly2.models.entities.Transactions;
import com.project.spendly2.models.entities.Users;
import com.project.spendly2.models.enums.TransactionCategory;
import com.project.spendly2.models.enums.TransactionType;
import com.project.spendly2.services.implementation.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class TransactionListener implements ApplicationListener<TransferFeedbackEvent> {

    private final EmailService emailService;

    @Override
    public void onApplicationEvent(TransferFeedbackEvent event) {
            Users theUser = event.getUsers();
            Transactions transactions = event.getTransactions();

        if (transactions.getTransactionCategory() == TransactionCategory.SPENDLY) {
            boolean isDebit = transactions.getUsers().equals(theUser);
            sendTransferEmail(theUser, transactions, isDebit);
        }
            try{
                boolean isDebit = transactions.getTransactionType() == TransactionType.DEBIT;
                sendTransferEmail(theUser, transactions, isDebit);
            }catch (RuntimeException e){
                throw new RuntimeException(e);
            }

    }

    public void sendTransferEmail(Users theUser, Transactions transactions, boolean isDebit){

        String subject = isDebit ? "Debit Transsaction Notification" : "Credit Transaction Notification";

        String body = isDebit ? "<p> Hello, " + theUser.getFirstName() + "  <p> " +
                "<p> Your transfer of " + transactions.getAmount() + " to " + transactions.getBeneficiaryName() + " was successful <p>" :

        "<p> Hello, " + theUser.getFirstName() + "  <p> " +
                "<p> Your account has been credited with the sum of " + transactions.getAmount() + " by " + transactions.getUsers().getFirstName() + " " + transactions.getUsers().getLastName() + "<p>";
        emailService.sendMessage(theUser.getEmail(), subject, body);

        log.info("Transaction email has been sent to {}", theUser.getEmail());
    }
}
