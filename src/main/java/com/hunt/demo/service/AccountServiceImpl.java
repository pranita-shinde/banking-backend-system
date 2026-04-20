package com.hunt.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hunt.demo.dto.AccountRequestDTO;
import com.hunt.demo.dto.AccountResponseDTO;
import com.hunt.demo.dto.LoginRequestDTO;
import com.hunt.demo.dto.TransactionDTO;
import com.hunt.demo.entity.Account;
import com.hunt.demo.entity.Transaction;
import com.hunt.demo.exception.InsufficientBalanceException;
import com.hunt.demo.exception.InvalidCredentialsException;
import com.hunt.demo.exception.UserNotFoundException;
import com.hunt.demo.mapper.AccountMapper;
import com.hunt.demo.mapper.TransactionMapper;
import com.hunt.demo.repository.AccountRepo;
import com.hunt.demo.repository.TransactionRepo;
import com.hunt.demo.response.ApiResponse;

@Service
public class AccountServiceImpl implements AccountService{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	@Autowired
	private AccountRepo repo;
	@Autowired
	private TransactionRepo transRepo;

	//Create account
	@Override
	public AccountResponseDTO createAccount(AccountRequestDTO dto){
		Account account = AccountMapper.toEntity(dto);
		/*account.setName(dto.getName());
		account.setBalance(dto.getBalance());
		account.setUsername(dto.getUsername());
		account.setPassword(dto.getPassword());
		account.setPin(dto.getPin());
		
		account.setCreatedAt(LocalDateTime.now());*/
		
		Account saved = repo.save(account);
		
		return AccountMapper.toDTO(account);
	}
	
	//Get Account
	@Override
	public AccountResponseDTO getAccountById(int id) {
		Account acc = repo.findById(id)
				.orElseThrow(()-> new UserNotFoundException("Account not found with id: "+id));
		
		return AccountMapper.toDTO(acc);
	}
	
	//Deposit Money
	@Override
	public AccountResponseDTO deposit(int id, double amount) {
		logger.info("Deposit request received for accountId: {} with amount: {}", id, amount);

		if(amount <= 0) {
			logger.error("Invalid Deposit amount: {}", amount);
			throw new IllegalArgumentException("Amount must be greater than zero");
		}
		Account acc = repo.findById(id)
				.orElseThrow(()-> new UserNotFoundException("Account not found with id: "+id));
		
		acc.setBalance(acc.getBalance() + amount);
		Account updated = repo.save(acc);
		
		logger.info("Deposit successful for accountId: {}. New balance: {}", id, updated.getBalance());
		
		Transaction t = new Transaction();
		t.setAccountId(id);
		t.setType("DEPOSIT");
		t.setAmount(amount);
		t.setTimestamp(LocalDateTime.now());
		
		transRepo.save(t);
		
		return AccountMapper.toDTO(updated);
	}
	
	//Withdraw Money
	@Override
	public AccountResponseDTO withdraw(int id, double amount, Integer pin) {
		logger.info("Withdraw request for accountId: {} with amount: {}", id, amount);
		Account acc = repo.findById(id)
				.orElseThrow(()-> new UserNotFoundException("Account not found with id: "+id));
		
		if(!acc.getPin().equals(pin)) {
			logger.error("Invalid PIN attempt for accountId: {}", id);
			throw new InvalidCredentialsException("Invalid PIN");
		}
		
		if(acc.getBalance() < amount) {
			throw new InsufficientBalanceException("Insufficient balance");
		}
		
		acc.setBalance(acc.getBalance() - amount);
		
		Account updated = repo.save(acc);
		
		logger.info("Withdraw successful for accountId: {}. Remaining balance: {}", id, acc.getBalance());
		
		Transaction t = new Transaction();
		t.setAccountId(id);
		t.setType("WITHDRAW");
		t.setAmount(amount);
		t.setTimestamp(LocalDateTime.now());
		
		transRepo.save(t);
		
		return AccountMapper.toDTO(updated);
	}
	

	//login
	@Override
	public ApiResponse login(LoginRequestDTO dto) {
		logger.info("Login attempt for username: {}", dto.getUsername());
		Account acc = repo.findByUsername(dto.getUsername())
			.orElseThrow(()-> new UserNotFoundException("User Not Found"));
			
		if(!acc.getPassword().equals(dto.getPassword())) {
			logger.error("Invalid login for username: {}", dto.getUsername());
			throw new InvalidCredentialsException("Invalid password");	
		}
		
		if(!acc.getPin().equals(dto.getPin())) {
			logger.error("Invalid login for username: {}", dto.getUsername());
			throw new InvalidCredentialsException("Invalid PIN");	
		}
			
		logger.info("Login Successful for username: {}", dto.getUsername());
	return new ApiResponse("Login successful", 200);
	}
	
	//close account4
	@Override
	public ApiResponse closeAccount(int id) {
		Account acc = repo.findById(id)
				.orElseThrow(()-> new UserNotFoundException("Account not found with id: "+id));
		
		logger.info("Closing account with id: {}", id);
		acc.setStatus("CLOSED");
		repo.deleteById(id);
		return new ApiResponse("Account closed succuessfully", 200);
	}
		
	//transactions
	@Override
	public List<TransactionDTO> getTransactions(int accountId){
		if(!repo.existsById(accountId)) {
			throw new UserNotFoundException("Account not found with id: "+accountId);
		}
		List<Transaction> transactions = transRepo.findByAccountId(accountId);
		
		logger.info("Fetching transactions for accountId: {}", accountId);
		
		return transactions.stream().map(TransactionMapper::toDTO).toList();
	}
}
