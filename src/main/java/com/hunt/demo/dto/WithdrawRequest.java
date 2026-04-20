package com.hunt.demo.dto;

import jakarta.validation.constraints.NotNull;

public class WithdrawRequest {
	@NotNull
	private double amount;
	@NotNull
	private Integer pin;
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Integer getPin() {
		return pin;
	}
	public void setPin(Integer pin) {
		this.pin = pin;
	}	
}
