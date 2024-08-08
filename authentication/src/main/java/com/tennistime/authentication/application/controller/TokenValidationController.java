package com.tennistime.authentication.application.controller;

import com.tennistime.authentication.infrastructure.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/token")
@Tag(name = "Token Validation", description = "Operations related to JWT token validation")
public class TokenValidationController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/validate")
    @Operation(summary = "Validate JWT token")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> request) {
        System.out.println("\033[1;36m-------------------------------------------------\033[0m");
        System.out.println("\033[1;36m<<< Authentication TokenValidationController >>>>\033[0m");
        System.out.println("\033[1;36m-------------------------------------------------\033[0m");
        String token = request.get("token");
        Map<String, Object> response = new HashMap<>();

        if (jwtUtil.validateToken(token)) {
            response.put("username", jwtUtil.getUsernameFromToken(token));
            response.put("email", jwtUtil.getEmailFromToken(token));
            response.put("roles", jwtUtil.getRolesFromToken(token));
            response.put("valid", true);

            // Logging the response
            System.out.println("\033[1;36m----------------------------\033[0m");
            System.out.println("\033[1;36m[TokenValidationController] Token valid: " + token + "\033[0m");
            System.out.println("\033[1;36m[TokenValidationController] Response: " + response + "\033[0m");
            System.out.println("\033[1;36m----------------------------\033[0m");

            return ResponseEntity.ok(response);
        } else {
            response.put("valid", false);

            // Logging
            System.out.println("\033[1;31m----------------------------\033[0m");
            System.out.println("\033[1;31m[TokenValidationController] Token invalid: " + token + "\033[0m");
            System.out.println("\033[1;31m----------------------------\033[0m");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
