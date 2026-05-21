package com.hunt.demo.response;

import java.time.LocalDateTime;

public class ApiResponse {

    private String msg;
    private int status;
    private LocalDateTime timestamp;
    private Object data;

    // ✅ Constructor with data
    public ApiResponse(String msg, int status, Object data) {
        this.msg = msg;
        this.status = status;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // ✅ Constructor without data
    public ApiResponse(String msg, int status) {
        this.msg = msg;
        this.status = status;
        this.timestamp = LocalDateTime.now();
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

    public Object getData() {
        return data;
    }
}