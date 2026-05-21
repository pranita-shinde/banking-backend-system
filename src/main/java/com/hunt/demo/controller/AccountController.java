package com.hunt.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.hunt.demo.dto.AccountRequestDTO;
import com.hunt.demo.dto.AccountResponseDTO;
import com.hunt.demo.dto.AmountDTO;
import com.hunt.demo.dto.LoginRequestDTO;
import com.hunt.demo.dto.TransactionDTO;
import com.hunt.demo.dto.TransferRequestDTO;
import com.hunt.demo.dto.TransferResponseDTO;
import com.hunt.demo.dto.WithdrawRequest;
import com.hunt.demo.entity.Transaction;
import com.hunt.demo.response.ApiResponse;
import com.hunt.demo.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	private final AccountService service;
	
	public AccountController(AccountService service) {
		super();
		this.service = service;
	}

	//create account
	@PostMapping
	public ResponseEntity<AccountResponseDTO> create(@Valid @RequestBody AccountRequestDTO dto){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(service.createAccount(dto));
	}
	
	//get account
	@GetMapping("/{id}")
	public AccountResponseDTO get(@PathVariable int id) {
		return service.getAccountById(id);
	}
	
	//Deposit
	@PutMapping("/{id}/deposit")
	public AccountResponseDTO deposit(@PathVariable int id, 
									 @RequestBody AmountDTO amountDTO) {
		return service.deposit(id, amountDTO.getAmount());
	}
	
	//Withdraw
	@PutMapping("/{id}/withdraw")
	public AccountResponseDTO withdraw(
			@PathVariable int id, 
			@RequestBody WithdrawRequest request) {
		
		return service.withdraw(id, request.getAmount(), request.getPin());
	}
	
	//Delete
	@DeleteMapping("/{id}")
	public ApiResponse delete(@PathVariable int id) {
		return service.closeAccount(id);
	}
	
	//transactions
	@GetMapping("/{id}/transactions")
	public Page<TransactionDTO> getTransactions(
        	@PathVariable int id,
        	@RequestParam(defaultValue = "0") int page,
        	@RequestParam(defaultValue = "5") int size) {

    	return service.getTransactions(id, page, size);
	}
	/*@GetMapping("/{id}/transactions")
	public List<TransactionDTO> getTransactions(@PathVariable int id){
		return service.getTransactions(id);
	}*/

	//transfer
	@PostMapping("/transfer")
	public ApiResponse transfer(@RequestBody TransferRequestDTO dto) {

    TransferResponseDTO response = service.transferMoney(dto);

    return new ApiResponse("Money transferred successfully",200,response);

	}
}
