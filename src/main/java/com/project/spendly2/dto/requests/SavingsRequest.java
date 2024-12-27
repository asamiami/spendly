package com.project.spendly2.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.spendly2.models.enums.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public record SavingsRequest(

        String name,

        Long amount,

        @Enumerated(EnumType.STRING)
        Category category,

        @JsonProperty(value = "time")
        LocalDate start,

        LocalDate endDate
) {
}
