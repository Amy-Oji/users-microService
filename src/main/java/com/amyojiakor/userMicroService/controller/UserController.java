package com.amyojiakor.userMicroService.controller;

import com.amyojiakor.userMicroService.models.payloads.UpdateUserDetailsDto;
import com.amyojiakor.userMicroService.models.payloads.UserDetailsResponse;
import com.amyojiakor.userMicroService.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get-user-details")
    public ResponseEntity<?> getUser() throws Exception {
        return ResponseEntity.ok(userService.getUserDetails());
    }

    @PostMapping("/update-user")
    public ResponseEntity<?> updateUserDetails(@RequestBody UpdateUserDetailsDto userDetailsDto) throws Exception {
        return ResponseEntity.ok(userService.updateUserDetails(userDetailsDto));
    }
}
