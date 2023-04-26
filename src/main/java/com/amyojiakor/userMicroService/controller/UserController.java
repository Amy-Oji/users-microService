package com.amyojiakor.userMicroService.controller;

import com.amyojiakor.userMicroService.models.payloads.CurrentUserDetailsResponse;
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
    public ResponseEntity<CurrentUserDetailsResponse> getUser() throws Exception {
        return ResponseEntity.ok(userService.getCurrentUserDetails());
    }

}
