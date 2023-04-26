package com.amyojiakor.userMicroService.services;

import com.amyojiakor.userMicroService.models.payloads.AccountResponse;
import com.amyojiakor.userMicroService.models.payloads.CurrentUserDetailsResponse;
import com.amyojiakor.userMicroService.models.payloads.UpdateAccountRequest;

public interface UserService {
    AccountResponse updateBankAccount(UpdateAccountRequest accountRequest);
    CurrentUserDetailsResponse getCurrentUserDetails() throws Exception;

}
