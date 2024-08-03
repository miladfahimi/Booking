package com.tennistime.backend.application.controller;

import com.tennistime.backend.application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private AuthService authService;

    @GetMapping("/feign")
    public ResponseEntity<String> testFeignClient() {
        String response = authService.callTestEndpoint();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Public Endpoint");
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
