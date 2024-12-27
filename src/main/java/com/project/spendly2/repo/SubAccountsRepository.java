package com.project.spendly2.repo;

import com.project.spendly2.models.entities.SavingsAccount;
import com.project.spendly2.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubAccountsRepository extends JpaRepository<SavingsAccount, Long> {


    Optional<SavingsAccount> findByUsers(Users users);
}
