package com.amyojiakor.userMicroService.services;

import com.amyojiakor.userMicroService.models.payloads.UserDetailsResponse;
import com.amyojiakor.userMicroService.models.payloads.UpdateUserDetailsDto;

public interface UserService {

    UserDetailsResponse getUserDetails() throws Exception;
    UpdateUserDetailsDto updateUserDetails(UpdateUserDetailsDto updateUserDetailsDto) throws Exception;
}
