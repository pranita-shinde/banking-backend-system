package com.hunt.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hunt.demo.response.ApiResponse;



@RestControllerAdvice
public class GlobalExceptionalHandler {
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex){
		return new ResponseEntity<>(
			new ApiResponse(ex.getMessage(), 404),
			HttpStatus.NOT_FOUND		
		);
	}
	
	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<?> handleBalance(InsufficientBalanceException ex){
		return new ResponseEntity<>(
			new ApiResponse(ex.getMessage(), 400),
			HttpStatus.BAD_REQUEST	
		);
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<?> handleInvalid(InvalidCredentialsException ex){
		return new ResponseEntity<>(
			new ApiResponse(ex.getMessage(), 401),
			HttpStatus.UNAUTHORIZED	
		);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneral(Exception ex){
		return new ResponseEntity<>(
			new ApiResponse("Something went wrong", 500),
			HttpStatus.INTERNAL_SERVER_ERROR	
		);
	}	
}
