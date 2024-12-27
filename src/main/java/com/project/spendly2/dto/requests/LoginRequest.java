package com.project.spendly2.dto.requests;

public record LoginRequest(
        String email,

        String password
) {
}
