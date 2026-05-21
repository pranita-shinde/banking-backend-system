package com.hunt.demo.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hunt.demo.dto.LoginRequestDTO;
import com.hunt.demo.entity.Account;
import com.hunt.demo.response.ApiResponse;
import com.hunt.demo.service.AccountService;
import com.hunt.demo.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
    private JwtUtil jwtUtil;
	
	@Autowired
	private AccountService service;
	
	//Login
	/*@PostMapping("/login")
	public ApiResponse login(@RequestBody LoginRequestDTO dto) {
		return service.login(dto);
	}*/

	@PostMapping("/login")
	public ApiResponse login(@RequestBody LoginRequestDTO dto) {

    // Authenticate user
    Account account = service.authenticate(dto); // you may need to rename login() → authenticate()

    // Generate JWT token
	System.out.println("Reached before token generation");
    String token = jwtUtil.generateToken(
		account.getUsername(), 
		account.getRole()
	);
	System.out.println("Token generated: " + token);

    // Return response
    // return new ApiResponse("Login successful", token);
	Map<String, String> response = new HashMap<>();
		response.put("token", token);

	return new ApiResponse("Login successful", 200, response);
	}

}
