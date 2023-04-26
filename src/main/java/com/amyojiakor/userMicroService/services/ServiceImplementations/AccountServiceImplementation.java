package com.amyojiakor.userMicroService.services.ServiceImplementations;

import com.amyojiakor.userMicroService.appConfig.ApiConfig;
import com.amyojiakor.userMicroService.models.payloads.*;
import com.amyojiakor.userMicroService.respositories.UserAccountRepository;
import com.amyojiakor.userMicroService.services.AccountService;
import com.amyojiakor.userMicroService.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
@Service
@RequiredArgsConstructor
public class AccountServiceImplementation implements AccountService {
    private final ApiConfig apiConfig;
    private final WebClient accountsServiceWebClient;
    private final RestTemplate restTemplate;

    private final UserService userService;
    private final UserAccountRepository accountRepository;


    @Override
    public AccountResponse createBankAccount(AccountRequest accountRequest) throws Exception {
        if(SecurityContextHolder.getContext().getAuthentication() == null) throw new Exception("User is not authenticated");

        CurrentUserResponse user = userService.getCurrentUser();

        CreateAccountPayLoad accountPayLoad = new CreateAccountPayLoad(
                user.firstName() + " " + user.lastName(),
                accountRequest.accountType(),
                accountRequest.currencyCode());
        AccountResponse account = restTemplate.postForObject(apiConfig.getAccountServiceBaseUrl()+"create-account", accountPayLoad,  AccountResponse.class);

//        if(account != null){
//            setUserAccount(user, account);
//        }

        return account;
    }

    @Override
    public AccountResponse updateBankAccount(UpdateAccountRequest accountRequest) {

        return null;
    }

//    private void setUserAccount (User user, AccountResponse accountResponse ){
//        UserAccounts userAccounts = new UserAccounts();
//        userAccounts.setUser(user);
//        userAccounts.setAccountBalance(accountResponse.accountBalance());
//        userAccounts.setAccountNumber(accountResponse.accountNumber());
//        userAccounts.setAccountType(accountResponse.accountType());
//        userAccounts.setCurrencyCode(accountResponse.currencyCode());
//        accountRepository.save(userAccounts);
//    }
}
