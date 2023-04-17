package com.amyojiakor.userMicroService.services;

import com.amyojiakor.userMicroService.models.entities.User;
import com.amyojiakor.userMicroService.models.payloads.AuthenticationRequest;
import com.amyojiakor.userMicroService.models.payloads.AuthenticationResponse;
import com.amyojiakor.userMicroService.models.payloads.RegisterRequest;

public interface UserService {
    AuthenticationResponse registerUser(RegisterRequest registerRequest) throws Exception;
    AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws Exception;
    User getCurrentUser() throws Exception;

}
