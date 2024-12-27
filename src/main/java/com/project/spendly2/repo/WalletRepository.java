package com.project.spendly2.repo;

import com.project.spendly2.models.entities.Users;
import com.project.spendly2.models.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {



    // Find wallet by the walletOwner object
    Optional<Wallet> findByUsers(Users users);

    
}
