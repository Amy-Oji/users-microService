package com.amyojiakor.userMicroService.services.ServiceImplementations;

import com.amyojiakor.userMicroService.models.entities.Roles;
import com.amyojiakor.userMicroService.models.entities.User;
import com.amyojiakor.userMicroService.models.payloads.AuthenticationRequest;
import com.amyojiakor.userMicroService.models.payloads.AuthenticationResponse;
import com.amyojiakor.userMicroService.models.payloads.RegisterRequest;
import com.amyojiakor.userMicroService.models.payloads.UpdatePasswordDto;
import com.amyojiakor.userMicroService.respositories.UserRepository;
import com.amyojiakor.userMicroService.security.jwt.JwtUtils;
import com.amyojiakor.userMicroService.security.user.UserDetailsImplementation;
import com.amyojiakor.userMicroService.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class AuthServiceImplementation implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse registerUser(RegisterRequest registerRequest) throws Exception {
        String email = registerRequest.email();

        if(!isValidEmail(email)){
            throw new Exception("Invalid email signature. Please enter a valid email");
        }

        if(userRepository.findByEmail(email).isPresent()){
            throw new Exception("You are already registered. Kindly login using this email");
        }

        User user = new User();
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Roles.USER);
        userRepository.save(user);

        AuthenticationRequest authRequest = new AuthenticationRequest(email, registerRequest.password());
        return login(authRequest);
    }


    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.email(),
                            authenticationRequest.password()
                    )
            );

            var user = userRepository.findByEmail(authenticationRequest.email())
                    .orElseThrow(() -> new Exception("User not found"));

            UserDetailsImplementation userDetails = new UserDetailsImplementation(user);

            System.out.println(userDetails);

            var jwt = jwtUtils.generateToken(userDetails);
            return AuthenticationResponse
                    .builder()
                    .token(jwt)
                    .role(user.getRole().name())
                    .build();

        } catch (Exception e) {
          throw new Exception ("incorrect username or password!");
        }
    }

    public void updatePassword(UpdatePasswordDto updatePasswordDto) throws Exception {
        System.out.println("in implementation");

        var currentUser = getCurrentUser2();
        System.out.println(currentUser);
        var user = userRepository.findByEmail(currentUser.getUsername()).orElseThrow(() -> new Exception("User not found"));

        if (!passwordEncoder.matches(updatePasswordDto.currentPassword(), user.getPassword())) {
            throw new Exception("Invalid current password");
        }
        user.setPassword(passwordEncoder.encode(updatePasswordDto.newPassword()));

        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("User not authenticated");
        }
        UserDetailsImplementation userDetails = (UserDetailsImplementation) authentication.getPrincipal();

        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new Exception("User not found"));
    }


    public UserDetails getCurrentUser2() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("User not authenticated");
        }
        return (UserDetails) authentication.getPrincipal();
    }


    private static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
