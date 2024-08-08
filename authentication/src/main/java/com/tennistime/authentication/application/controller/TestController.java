package com.tennistime.authentication.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/feign")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Hello from Authentication Service");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("Admin Endpoint");
    }

    @GetMapping("/club")
    public ResponseEntity<String> clubOwnerEndpoint() {
        return ResponseEntity.ok("Club Owner Endpoint");
    }

    @GetMapping("/user")
    public ResponseEntity<String> userEndpoint() {
        return ResponseEntity.ok("User Endpoint");
    }
}