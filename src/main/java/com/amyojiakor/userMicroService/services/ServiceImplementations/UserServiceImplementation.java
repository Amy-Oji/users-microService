package com.amyojiakor.userMicroService.services.ServiceImplementations;

import com.amyojiakor.userMicroService.appConfig.ApiConfig;
import com.amyojiakor.userMicroService.models.entities.UserAccounts;
import com.amyojiakor.userMicroService.models.payloads.*;
import com.amyojiakor.userMicroService.respositories.UserAccountRepository;
import com.amyojiakor.userMicroService.respositories.UserRepository;
import com.amyojiakor.userMicroService.security.user.UserDetailsImplementation;
import com.amyojiakor.userMicroService.services.UserService;
import com.amyojiakor.userMicroService.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final ApiConfig apiConfig;
    private final WebClient accountsServiceWebClient;
    private final RestTemplate restTemplate;

    private final AuthService authService;
    private final UserAccountRepository accountRepository;

    private final UserRepository userRepository;

    public CurrentUserDetailsResponse getCurrentUserDetails() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("User not authenticated");
        }
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();

        var user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new Exception("User not found"));

        List<UserAccounts> allUserAccounts = user.getAccounts();

        return new CurrentUserDetailsResponse(user.getFirstName(), user.getLastName(), user.getEmail(),  allUserAccounts);
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
