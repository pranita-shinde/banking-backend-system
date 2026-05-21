package com.hunt.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hunt.demo.response.ApiResponse;
import com.hunt.demo.service.AccountService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AccountService service;

    // Test API
    @GetMapping("/test")
    public String adminTest() {
        return "Only ADMIN can access this";
    }

    // 🔓 Unlock Account API
    @PutMapping("/unlock/{id}")
    public ApiResponse unlockAccount(@PathVariable int id) {
        return service.unlockAccount(id);
    }
}