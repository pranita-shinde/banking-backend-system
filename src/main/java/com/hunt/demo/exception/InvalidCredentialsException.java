package com.hunt.demo.exception;

public class InvalidCredentialsException extends RuntimeException{
	public InvalidCredentialsException(String msg) {
		super(msg);
	}
}
