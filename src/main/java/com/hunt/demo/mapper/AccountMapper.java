package com.hunt.demo.mapper;

import java.time.LocalDateTime;

import com.hunt.demo.dto.AccountRequestDTO;
import com.hunt.demo.dto.AccountResponseDTO;
import com.hunt.demo.entity.Account;

public class AccountMapper {
	//DTO-->Entity
	public static Account toEntity(AccountRequestDTO dto) {
		Account acc = new Account();
		
		acc.setName(dto.getName());
		acc.setUsername(dto.getUsername());
		acc.setPassword(dto.getPassword());
		acc.setPin(dto.getPin());
		acc.setBalance(dto.getBalance());
		acc.setCreatedAt(LocalDateTime.now());
		acc.setStatus("ACTIVE");
		return acc;
	}
	
	//Entity-->DTO
	public static AccountResponseDTO toDTO(Account acc) {
		AccountResponseDTO dto = new AccountResponseDTO();
		dto.setId(acc.getId());
		dto.setName(acc.getName());
		dto.setUsername(acc.getUsername());
		dto.setBalance(acc.getBalance());
		return dto;
	}
}
