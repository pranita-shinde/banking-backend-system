package com.hunt.demo.service;

import java.util.List;

import com.hunt.demo.dto.AccountRequestDTO;
import com.hunt.demo.dto.AccountResponseDTO;
import com.hunt.demo.dto.LoginRequestDTO;
import com.hunt.demo.dto.TransactionDTO;
import com.hunt.demo.dto.TransferRequestDTO;
import com.hunt.demo.dto.TransferResponseDTO;
import com.hunt.demo.entity.Account;
import com.hunt.demo.entity.Transaction;
import com.hunt.demo.response.ApiResponse;
import org.springframework.data.domain.Page;

public interface AccountService{
	AccountResponseDTO createAccount(AccountRequestDTO dto);
	
	AccountResponseDTO getAccountById(int id);
	
	AccountResponseDTO deposit(int id, double amount);
	
	AccountResponseDTO withdraw(int id, double amount, Integer pin);
	
	//ApiResponse login(LoginRequestDTO dto);
	Account authenticate(LoginRequestDTO dto);
	
	ApiResponse closeAccount(int id);

	Page<TransactionDTO> getTransactions(int accountId, int page, int size);
	//List<TransactionDTO> getTransactions(int accountId);

	TransferResponseDTO transferMoney(TransferRequestDTO dto);

	ApiResponse unlockAccount(int accountId);
	
}
