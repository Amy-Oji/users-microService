package com.amyojiakor.userMicroService.controller;

import com.amyojiakor.userMicroService.models.payloads.AccountRequest;
import com.amyojiakor.userMicroService.models.payloads.AccountResponse;
import com.amyojiakor.userMicroService.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create-account")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest accountRequest) throws Exception{
        return ResponseEntity.ok((accountService.createBankAccount(accountRequest)));
    }


}
