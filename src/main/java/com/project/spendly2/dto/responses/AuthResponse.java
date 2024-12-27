package com.project.spendly2.dto.responses;

import lombok.Builder;

@Builder
public record AuthResponse
    (String token,

    String firstName,
    String lastName,
    String email){

}
