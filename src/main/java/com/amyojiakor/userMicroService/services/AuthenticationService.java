package com.amyojiakor.userMicroService.services;

import com.amyojiakor.userMicroService.models.entities.User;
import com.amyojiakor.userMicroService.models.payloads.*;

public interface AuthenticationService {
    AuthenticationResponse registerUser(RegisterRequest registerRequest) throws Exception;
    AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws Exception;
    User getCurrentUser() throws Exception;
    PasswordUpdateResponse updatePassword(UpdatePasswordDto updatePasswordDto) throws Exception;
}
