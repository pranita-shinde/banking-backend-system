package com.hunt.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hunt.demo.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Integer>{
	List<Transaction> findByAccountId(int accountId);
}
