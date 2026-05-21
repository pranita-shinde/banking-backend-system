package com.hunt.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.hunt.demo.dto.AccountRequestDTO;
import com.hunt.demo.dto.AccountResponseDTO;
import com.hunt.demo.dto.LoginRequestDTO;
import com.hunt.demo.dto.TransactionDTO;
import com.hunt.demo.dto.TransferRequestDTO;
import com.hunt.demo.dto.TransferResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


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
		account.setRole("USER"); // default
		
		return AccountMapper.toDTO(account);
	}
	
	//Get Account
	@Override
	public AccountResponseDTO getAccountById(int id) {
		Account acc = repo.findById(id)
				.orElseThrow(()-> new UserNotFoundException("Account not found with id: "+id));
		
		return AccountMapper.toDTO(acc);
	}
	
	//Deposit Money (UPDATED WITH INTEREST)
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public AccountResponseDTO deposit(int id, double amount) {

    logger.info("Deposit request received for accountId: {} with amount: {}", id, amount);

    if (amount <= 0) {
        logger.error("Invalid Deposit amount: {}", amount);
        throw new IllegalArgumentException("Amount must be greater than zero");
    }

    Account acc = repo.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Account not found with id: " + id));

    // 💰 Interest calculation (2.5%)
    double interestRate = 2.5;
    double interest = amount * interestRate / 100;
    double totalAmount = amount + interest;

    // 💸 Update balance
    acc.setBalance(acc.getBalance() + totalAmount);
    Account updated = repo.save(acc);

    logger.info("Deposit successful for accountId: {}. Amount: {}, Interest: {}, New balance: {}",
            id, amount, interest, updated.getBalance());

    // 🧾 Transaction entry
    Transaction t = new Transaction();
    t.setAccountId(id);
    t.setType("DEPOSIT");
    t.setAmount(totalAmount); // total credited
    t.setTimestamp(LocalDateTime.now());
    t.setRemarks("Deposit: " + amount + " | Interest: " + interest);

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
		t.setRemarks("Withdrawn from account " + id);
		
		transRepo.save(t);
		
		return AccountMapper.toDTO(updated);
	}
	
	//login
	@Override
	public Account authenticate(LoginRequestDTO dto) {

    	logger.info("Login attempt for username: {}", dto.getUsername());

    	Account acc = repo.findByUsername(dto.getUsername())
            .orElseThrow(() -> new UserNotFoundException("User Not Found"));

    	// 🔒 CHECK IF ACCOUNT IS LOCKED
    	if (acc.isLocked()) {

        	// ⏳ Check if 15 minutes passed
        	if (acc.getLockTime() != null &&
            	acc.getLockTime().plusMinutes(15).isBefore(LocalDateTime.now())) {

            	// ✅ AUTO UNLOCK
            	acc.setLocked(false);
            	acc.setFailedAttempts(0);
            	acc.setLockTime(null);
            	repo.save(acc);

            	logger.info("Account auto-unlocked for username: {}", dto.getUsername());

        	} else {
            	logger.error("Account is locked for username: {}", dto.getUsername());
            	throw new RuntimeException("Account is locked. Try again after some time.");
        	}
    	}

    	// ❌ INVALID PASSWORD OR PIN
    	if (!acc.getPassword().equals(dto.getPassword()) ||
        	!acc.getPin().equals(dto.getPin())) {

        	acc.setFailedAttempts(acc.getFailedAttempts() + 1);

        	// 🔒 Lock after 3 attempts
        	if (acc.getFailedAttempts() >= 3) {
            	acc.setLocked(true);
            	acc.setLockTime(LocalDateTime.now());

            	logger.error("Account locked due to multiple failed attempts: {}", dto.getUsername());
        	}

        	repo.save(acc);
        	throw new InvalidCredentialsException("Invalid credentials");
    	}

    	// ✅ SUCCESS LOGIN
    	acc.setFailedAttempts(0);
    	acc.setLockTime(null);
    	repo.save(acc);

    	logger.info("Login successful for username: {}", dto.getUsername());

    	return acc;
	}
	
	//close account
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
	public Page<TransactionDTO> getTransactions(int accountId, int page, int size) {

    	if (!repo.existsById(accountId)) {
        	throw new UserNotFoundException("Account not found with id: " + accountId);
    	}

    	Pageable pageable = PageRequest.of(
        	page,
        	size,
        	Sort.by("timestamp").descending()
    	);

    	Page<Transaction> transactions =
        	transRepo.findByAccountIdOrderByTimestampDesc(accountId, pageable);

    	logger.info("Fetching transactions for accountId: {} page: {} size: {}", accountId, page, size);

    	return transactions.map(TransactionMapper::toDTO);
	}
	/*@Override
	public List<TransactionDTO> getTransactions(int accountId){
		if(!repo.existsById(accountId)) {
			throw new UserNotFoundException("Account not found with id: "+accountId);
		}
		List<Transaction> transactions =
            transRepo.findByAccountIdOrderByTimestampDesc(accountId);
		
		logger.info("Fetching transactions for accountId: {}", accountId);
		
		return transactions.stream().map(TransactionMapper::toDTO).toList();
	}*/

	//transfer money
	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public TransferResponseDTO transferMoney(TransferRequestDTO dto) {

    	int fromId = dto.getFromAccountId().intValue();
    	int toId = dto.getToAccountId().intValue();

    	Account fromAcc;
    	Account toAcc;

    	// 🔒 LOCK IN SAME ORDER (PREVENT DEADLOCK)
    	if (fromId < toId) {
        	fromAcc = repo.findByIdForUpdate(fromId);
        	toAcc = repo.findByIdForUpdate(toId);
    	} else {
        	toAcc = repo.findByIdForUpdate(toId);
        	fromAcc = repo.findByIdForUpdate(fromId);
    	}

    	// NULL CHECK (IMPORTANT)
    	if (fromAcc == null) {
        	throw new RuntimeException("Sender account not found");
    	}

    	if (toAcc == null) {
        	throw new RuntimeException("Receiver account not found");
    	}

    	// PIN validation
    	if (!fromAcc.getPin().equals(dto.getPin())) {
        	throw new RuntimeException("Invalid PIN");
    	}

    	// Balance check
    	if (fromAcc.getBalance() < dto.getAmount()) {
        	throw new RuntimeException("Insufficient balance");
    	}

    	// 💸 Deduct & Add
    	fromAcc.setBalance(fromAcc.getBalance() - dto.getAmount());
    	toAcc.setBalance(toAcc.getBalance() + dto.getAmount());

    	repo.save(fromAcc);
    	repo.save(toAcc);

    	// 🧾 DEBIT (sender)
    	Transaction debitTxn = new Transaction();
    	debitTxn.setAccountId(fromAcc.getId());
    	debitTxn.setAmount(dto.getAmount());
    	debitTxn.setType("DEBIT");
    	debitTxn.setTimestamp(LocalDateTime.now());
    	debitTxn.setRemarks("Transfer to account " + toAcc.getId());

    	transRepo.save(debitTxn);

    	// 🧾 CREDIT (receiver)
    	Transaction creditTxn = new Transaction();
    	creditTxn.setAccountId(toAcc.getId());
    	creditTxn.setAmount(dto.getAmount());
    	creditTxn.setType("CREDIT");
    	creditTxn.setTimestamp(LocalDateTime.now());
    	creditTxn.setRemarks("Received from account " + fromAcc.getId());

    	transRepo.save(creditTxn);

    	return new TransferResponseDTO(
            "Transfer Successful",
            fromAcc.getBalance()
    	);
	}

    //Unlock Account (Admin)
	@Override
	public ApiResponse unlockAccount(int accountId) {

    	Account acc = repo.findById(accountId)
            	.orElseThrow(() -> new UserNotFoundException("Account not found"));

    	if (!acc.isLocked()) {
        	return new ApiResponse("Account is already active", 200);
    	}

    	acc.setLocked(false);
    	acc.setFailedAttempts(0);
    	acc.setLockTime(null);

    	repo.save(acc);

    	logger.info("Account unlocked by ADMIN: {}", accountId);

    	return new ApiResponse("Account unlocked successfully", 200);
	}
}
