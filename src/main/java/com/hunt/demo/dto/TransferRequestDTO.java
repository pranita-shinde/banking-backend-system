package com.hunt.demo.dto;

public class TransferRequestDTO {
    private Long fromAccountId;
    private Long toAccountId;
    private double amount;
    private Integer pin;

    // Getters and setters 
     public Long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }

    public Long getToAccountId() { return toAccountId; }
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Integer getPin() { return pin; }
    public void setPin(Integer pin) { this.pin = pin; } 
}
