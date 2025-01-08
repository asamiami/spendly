package com.project.spendly2.services.implementation;



import com.project.spendly2.dto.requests.SpendlyTransferRequest;
import com.project.spendly2.dto.requests.TransferRequest;
import com.project.spendly2.dto.responses.AllBankData;
import com.project.spendly2.dto.responses.ApiResponse;
import com.project.spendly2.dto.responses.TransferResponse;
import com.project.spendly2.events.TransferFeedbackEvent;
import com.project.spendly2.models.entities.Transactions;
import com.project.spendly2.models.entities.Users;
import com.project.spendly2.models.entities.Wallet;
import com.project.spendly2.models.enums.TransactionCategory;
import com.project.spendly2.models.enums.TransactionStatus;
import com.project.spendly2.models.enums.TransactionType;
import com.project.spendly2.repo.TransactionRepository;
import com.project.spendly2.repo.UserRepository;
import com.project.spendly2.repo.WalletRepository;
import com.project.spendly2.services.WalletServices;
import com.project.spendly2.util.FLWUtill;
import com.project.spendly2.util.UserUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletServices {

    private final FLWUtill flutterwaveService;
    private final WalletRepository walletRepo;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final ApplicationEventPublisher publisher;
    private final UserUtil userUtil;
    private final PasswordEncoder passwordEncoder;

    protected String generateTxRef() {
        return "SPND-" + UUID.randomUUID().toString().substring(0, 6);
    }

    public Wallet createWallet(Users user) {

        if (user.getId() != null) {
            user = userRepository.save(user);
        }
        Wallet wallet = flutterwaveService.createWallet(
                user.getEmail(), user.getBvn(),
                generateTxRef(), user.getLastName(),
                user.getFirstName(), user.getPhonenumber());
        wallet.addUser(user);

        return walletRepo.save(wallet);
    }


    public ApiResponse<String> spendlyToSpendlyTransfer (SpendlyTransferRequest request){
        String loggedInUser = UserUtil.getLoginUser();
        List<Transactions>transactions = new ArrayList<>();
       Users user = userRepository.findByEmail(loggedInUser).orElseThrow();
       Optional<Users> username = userRepository.findByUsername(request.username());
       Optional<Wallet> wallet = walletRepo.findByUsers(user);
       Optional<Wallet> benficiaryWallet = walletRepo.findByUsers(username.get());
        Transactions transaction = new Transactions();

      verifyPin(request.pin());

       if (!userUtil.isBalanceSufficient(request.amount())) {
           return new ApiResponse<>("Balance is not sufficient", HttpStatus.BAD_REQUEST);
       }

       if (wallet.isPresent() && userUtil.isBalanceSufficient(request.amount()))
           {
            transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
            transaction.setAccountNumber(request.username());
            transaction.setAmount(request.amount());
            transaction.setBeneficiaryName(user.getFirstName() + " " + user.getLastName());
            transaction.setNarration(request.narration());
            transaction.setTransactionCategory(TransactionCategory.SPENDLY);
            transaction.setReference(generateTxRef());
            transaction.setCreatedDate(LocalDateTime.now());
            transaction.setBeneficiaryBank("Spendly");
            transaction.setUsers(user);
            transactions.add(transaction);
            transaction.setTransactionType(TransactionType.DEBIT);
            user.setTransactions(transactions);
            transactionRepository.save(transaction);
            transactionRepository.save(transaction);
            wallet.get().updateBalance(request.amount(), true);
            benficiaryWallet.get().updateBalance(request.amount(), false);
            walletRepo.save(wallet.get());
            walletRepo.save(benficiaryWallet.get());
            publisher.publishEvent(new TransferFeedbackEvent(user, transaction, true));
            publisher.publishEvent(new TransferFeedbackEvent(benficiaryWallet.get().getUsers(), transaction, false));
            return new ApiResponse<>("The transfer of" + request.amount() + " to " + username.get().getFirstName() + "  was successful", HttpStatus.OK );
        }else {
           transaction.setTransactionStatus(TransactionStatus.FAILED);
           transactionRepository.save(transaction);
           return new ApiResponse<>();
       }
    }


    public ApiResponse<TransferResponse> transferToBank(TransferRequest request) {

        String loginUserEmail = UserUtil.getLoginUser();
        Users user = userRepository.findByEmail(loginUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Wallet wallet = walletRepo.findByUsers(user)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user"));

        List<Transactions>transactions = new ArrayList<>();


        BigDecimal amount = request.amount();
        if (!userUtil.isBalanceSufficient(amount) ) {
            throw new RuntimeException("Insufficient balance");
        }

        Transactions transaction = new Transactions();
        transaction.setAccountNumber(request.accountNumber());
        transaction.setNarration(request.narration());
        transaction.setCreatedDate(LocalDateTime.now());
        transaction.setAmount(amount);
        transaction.setBankCode(request.bankCode());
        transaction.setReference(generateTxRef());
        transaction.setTransactionCategory(TransactionCategory.EXTERNAL);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setBeneficiaryBank(request.bankName());
        transaction.setTime(request.time());
        transaction.setBeneficiaryName(request.receiverName());
        transaction.setUsers(user);
        transactions.add(transaction);
        user.setTransactions(transactions);
        transactionRepository.save(transaction);
        userRepository.save(user);

        if((request.time().equals(LocalDateTime.now()) || request.time().isBefore(LocalDateTime.now()))){

            return executeImmediateTransfers(request);
        }else {
            return scheduleTransfer(transaction);
        }

    }

    public ApiResponse<TransferResponse> executeImmediateTransfers(TransferRequest request) {
        Transactions transaction = transactionRepository.findByTime(request.time());
        log.info(request.bankName());


        Transactions result = flutterwaveService.transferMoney(request, transaction.getReference());
        Optional<Users>  users = userRepository.findByEmail(transaction.getUsers().getEmail());
        log.info(users.get().getLastName());
        Wallet wallet = walletRepo.findByUsers(transaction.getUsers())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for user"));

        if (result.getTransactionStatus() == TransactionStatus.SUCCESSFUL) {
            transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);

            wallet.updateBalance(request.amount(), true);
            walletRepo.save(wallet);
            TransferResponse response = new TransferResponse(
                    request.receiverName(),
                    request.bankCode(),
                    request.bankName(),
                    request.accountNumber(),
                    request.amount(),
                    request.narration(),
                    transaction.getReference(),
                    "Successful",
                    "Your transfer was successful");

            publisher.publishEvent(new TransferFeedbackEvent(users.get(), transaction, true));

            return new ApiResponse<>("Transaction successful", HttpStatus.OK, response);
        } else {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            wallet.updateBalance(request.amount(), false);
            walletRepo.save(wallet);
            return new ApiResponse<>("Transaction failed", HttpStatus.BAD_REQUEST);
        }

    }

    public ApiResponse<String> verifyPin(String pin) {
        String email = UserUtil.getLoginUser();
        var user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        if(user.getTransactionPin() == null) {
            throw new RuntimeException("You have not created a pin yet");
        } else {
            if(passwordEncoder.matches(pin, user.getTransactionPin())) {
                return new ApiResponse<>("Pin matches", HttpStatus.OK);
            } else {
                throw new RuntimeException("Pin does not match");
            }
        }
    }


    private ApiResponse<TransferResponse> scheduleTransfer(Transactions transaction) {
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);

        return new ApiResponse<>("Transaction scheduled for " + transaction.getTime(), HttpStatus.ACCEPTED);
    }

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void processScheduledTransfers() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Scheduled task triggered at: " + now);

        List<Transactions> pendingTransactions = transactionRepository.findByTransactionStatus(TransactionStatus.PENDING);


        if (pendingTransactions.isEmpty()) {
            log.info("No pending transactions found for the scheduled time.");
            return;
        }

        log.info(pendingTransactions.size() + " pending transactions found.");
        for (Transactions transaction : pendingTransactions) {
            try {
                log.info("Processing transaction: " + transaction.getReference());

                log.info("Transaction time: " + transaction.getTime());
                log.info("Current time: " + now);

                if (transaction.getTime().isBefore(now.plusSeconds(30)) && transaction.getTime().isAfter(now.minusSeconds(30))) {
                    log.info("Executing scheduled transfer for transaction: " + transaction.getReference());
                    TransferRequest request = new TransferRequest(
                            transaction.getBankCode(),
                            transaction.getAccountNumber(),
                            transaction.getBeneficiaryName(),
                            transaction.getBeneficiaryBank(),
                            transaction.getAmount(),
                            transaction.getNarration(),
                            transaction.getTime(),
                            transaction.getUsers().getTransactionPin()

                    );
                    transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
                    executeImmediateTransfers(request);
                    transactionRepository.save(transaction);

                    log.info("Transaction completed successfully for reference: " + transaction.getReference());
                }
            } catch (Exception e) {
                log.error("Error processing transaction with reference: " + transaction.getReference(), e);
            }
        }
    }

}

