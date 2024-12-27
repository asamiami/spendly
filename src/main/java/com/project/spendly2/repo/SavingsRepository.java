package com.project.spendly2.repo;

import com.project.spendly2.models.entities.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsRepository extends JpaRepository<Savings,Long> {
}
