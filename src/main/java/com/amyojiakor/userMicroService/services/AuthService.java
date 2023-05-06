package com.amyojiakor.userMicroService.services;

import com.amyojiakor.userMicroService.models.entities.User;
import com.amyojiakor.userMicroService.models.payloads.AuthenticationRequest;
import com.amyojiakor.userMicroService.models.payloads.AuthenticationResponse;
import com.amyojiakor.userMicroService.models.payloads.RegisterRequest;
import com.amyojiakor.userMicroService.models.payloads.UpdatePasswordDto;

public interface AuthService {
    AuthenticationResponse registerUser(RegisterRequest registerRequest) throws Exception;
    AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws Exception;
    User getCurrentUser() throws Exception;
    void updatePassword(UpdatePasswordDto updatePasswordDto) throws Exception;
}
