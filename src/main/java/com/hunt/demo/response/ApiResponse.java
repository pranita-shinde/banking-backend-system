package com.hunt.demo.response;

import java.time.LocalDateTime;

public class ApiResponse {
	private String msg;
	private int status;
	private LocalDateTime timestamp;
	
	public ApiResponse(String msg, int status, LocalDateTime timestamp) {
		this.msg = msg;
		this.status = status;
		this.timestamp = timestamp;
	}

	public ApiResponse(String msg, int status) {
		this.msg = msg;
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public int getStatus() {
		return status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}	
}
