package com.amyojiakor.userMicroService.services;

import com.amyojiakor.userMicroService.models.payloads.AccountRequest;
import com.amyojiakor.userMicroService.models.payloads.AccountResponse;
import com.amyojiakor.userMicroService.models.payloads.UpdateAccountRequest;

public interface AccountService {
    AccountResponse updateBankAccount(UpdateAccountRequest accountRequest);
    AccountResponse createBankAccount(AccountRequest accountRequest) throws Exception;
}
