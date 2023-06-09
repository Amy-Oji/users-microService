package com.amyojiakor.userMicroService.controller;

import com.amyojiakor.userMicroService.models.payloads.AuthenticationRequest;
import com.amyojiakor.userMicroService.models.payloads.AuthenticationResponse;
import com.amyojiakor.userMicroService.models.payloads.RegisterRequest;
import com.amyojiakor.userMicroService.models.payloads.UpdatePasswordDto;
import com.amyojiakor.userMicroService.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) throws Exception {
        return ResponseEntity.ok(authenticationService.registerUser(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody AuthenticationRequest authRequest) throws Exception {
        return ResponseEntity.ok(authenticationService.login(authRequest));
    }
    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
        return ResponseEntity.ok(authenticationService.updatePassword(updatePasswordDto));
    }
}
