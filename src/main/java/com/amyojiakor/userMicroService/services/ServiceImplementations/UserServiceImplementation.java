package com.amyojiakor.userMicroService.services.ServiceImplementations;

import com.amyojiakor.userMicroService.models.entities.User;
import com.amyojiakor.userMicroService.models.payloads.*;
import com.amyojiakor.userMicroService.respositories.UserRepository;
import com.amyojiakor.userMicroService.services.AuthService;
import com.amyojiakor.userMicroService.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final AuthService authService;

    public UserDetailsResponse getUserDetails() throws Exception {
        User user = authService.getCurrentUser();
        return new UserDetailsResponse(user.getFirstName(), user.getLastName(), user.getEmail(),  user.getAccounts());
    }

//    Todo:
//    update updateUserDetails method... it accepts null email value which should not be.
    @Override
    public UpdateUserDetailsDto updateUserDetails(UpdateUserDetailsDto updateUserDetailsDto) throws Exception {
        User user = authService.getCurrentUser();
        BeanUtils.copyProperties(updateUserDetailsDto, user);
        userRepository.save(user);
        return updateUserDetailsDto;
    }
}
