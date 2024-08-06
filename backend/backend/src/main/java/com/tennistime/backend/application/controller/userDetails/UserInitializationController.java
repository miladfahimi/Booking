package com.tennistime.backend.application.controller.userDetails;

import com.tennistime.backend.application.dto.userDetails.UserInitializationRequestDTO;
import com.tennistime.backend.application.dto.userDetails.UserInitializationResponseDTO;
import com.tennistime.backend.application.service.UserInitializationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Initialization", description = "User Initialization Operations")
public class UserInitializationController {

    private final UserInitializationService userInitializationService;

    @PostMapping("/initialize")
    @Operation(summary = "Initialize User Entities")
    public ResponseEntity<UserInitializationResponseDTO> initializeUser(
            @RequestBody UserInitializationRequestDTO request) {

        UserInitializationResponseDTO response = userInitializationService.initializeUserEntities(request);
        return ResponseEntity.ok(response);
    }
}
