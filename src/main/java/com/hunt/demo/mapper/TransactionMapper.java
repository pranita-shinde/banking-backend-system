package com.hunt.demo.mapper;

import java.time.LocalDateTime;

import com.hunt.demo.dto.TransactionDTO;
import com.hunt.demo.entity.Transaction;

public class TransactionMapper {
	public static TransactionDTO toDTO(Transaction t) {
		TransactionDTO dto = new TransactionDTO();
		dto.setId(t.getId());
		dto.setAccountId(t.getAccountId());
		dto.setType(t.getType());
		dto.setAmount(t.getAmount());
		
		if(t.getTimestamp() != null) {
			dto.setTimestamp(t.getTimestamp().toString());
		}
		
		return dto;
	}
}
