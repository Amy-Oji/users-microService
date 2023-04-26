package com.amyojiakor.userMicroService.services.ServiceImplementations;

import com.amyojiakor.userMicroService.models.entities.UserAccounts;
import com.amyojiakor.userMicroService.models.payloads.*;
import com.amyojiakor.userMicroService.respositories.UserRepository;
import com.amyojiakor.userMicroService.security.user.UserDetailsImplementation;
import com.amyojiakor.userMicroService.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

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
}
