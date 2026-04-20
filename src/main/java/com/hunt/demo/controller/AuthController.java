package com.hunt.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hunt.demo.dto.LoginRequestDTO;
import com.hunt.demo.response.ApiResponse;
import com.hunt.demo.service.AccountService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AccountService service;
	
	//Login
	@PostMapping("/login")
	public ApiResponse login(@RequestBody LoginRequestDTO dto) {
		return service.login(dto);
	}

}
