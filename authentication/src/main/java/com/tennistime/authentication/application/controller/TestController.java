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

    @GetMapping("/provider")
    public ResponseEntity<String> providerOwnerEndpoint() {
        return ResponseEntity.ok("Provider Owner Endpoint");
    }

    @GetMapping("/user")
    public ResponseEntity<String> userEndpoint() {
        return ResponseEntity.ok("User Endpoint");
    }
}