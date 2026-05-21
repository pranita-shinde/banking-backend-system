package com.hunt.demo.dto;

public class TransferResponseDTO {

    private String message;
    private double remainingBalance;

    public TransferResponseDTO(String message, double remainingBalance) {
        this.message = message;
        this.remainingBalance = remainingBalance;
    }

    public String getMessage() { return message; }
    public double getRemainingBalance() { return remainingBalance; }

}
