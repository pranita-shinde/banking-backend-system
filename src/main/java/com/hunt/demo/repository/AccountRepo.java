package com.hunt.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hunt.demo.entity.Account;

public interface AccountRepo extends JpaRepository<Account, Integer>{
	Optional<Account> findByUsername(String username);
}
