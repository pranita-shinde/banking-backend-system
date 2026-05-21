package com.hunt.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hunt.demo.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepo extends JpaRepository<Transaction, Integer>{

	Page<Transaction> findByAccountIdOrderByTimestampDesc(int accountId, Pageable pageable);
	
	//List<Transaction> findByAccountIdOrderByTimestampDesc(int accountId);

	//List<Transaction> transactions = transRepo.findByAccountIdOrderByTimestampDesc(accountId);
}
