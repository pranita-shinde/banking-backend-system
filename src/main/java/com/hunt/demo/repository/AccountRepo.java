package com.hunt.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hunt.demo.entity.Account;

import jakarta.persistence.LockModeType;

public interface AccountRepo extends JpaRepository<Account, Integer>{
	Optional<Account> findByUsername(String username);

	// 🔒 PESSIMISTIC LOCK METHOD
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT a FROM Account a WHERE a.id = :id")
	Account findByIdForUpdate(@Param("id") int id);
}
