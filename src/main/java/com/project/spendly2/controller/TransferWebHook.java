package com.project.spendly2.controller;

import com.project.spendly2.dto.requests.FLWTransferRequest;
import com.project.spendly2.dto.responses.TransferResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")

public class TransferWebHook {
    @PostMapping("/transfer")
    public ResponseEntity<String> handleTransferWebhook(@RequestBody TransferResponse payload) {
        // Log incoming request for debugging
        System.out.println("Webhook received: " + payload);

        // Process the transfer event
        if ("SUCCESS".equalsIgnoreCase(payload.status())) {
            // Handle successful transfer
            System.out.println("Transfer successful for transaction ID: " + payload.reference());
        } else {
            // Handle failed or pending transfer
            System.out.println("Transfer failed or pending for transaction ID: " + payload.reference());
        }

        // Return a response to acknowledge receipt
        return ResponseEntity.ok("Transfer webhook processed");
    }

    @PostMapping("/transfer/secure")
    public ResponseEntity<String> handleTransferWebhook(
            @RequestHeader("X-Webhook-Token") String token,
            @RequestBody TransferResponse payload) {

        if (!"expected-secret-token".equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Process the webhook
        System.out.println("Valid webhook received: " + payload);

        return ResponseEntity.ok("Transfer webhook processed");
    }

}

