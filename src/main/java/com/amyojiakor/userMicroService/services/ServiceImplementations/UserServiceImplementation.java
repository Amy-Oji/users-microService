package com.amyojiakor.userMicroService.services.ServiceImplementations;

import com.amyojiakor.userMicroService.models.entities.Roles;
import com.amyojiakor.userMicroService.models.entities.User;
import com.amyojiakor.userMicroService.models.payloads.AuthenticationRequest;
import com.amyojiakor.userMicroService.models.payloads.AuthenticationResponse;
import com.amyojiakor.userMicroService.models.payloads.RegisterRequest;
import com.amyojiakor.userMicroService.respositories.UserRepository;
import com.amyojiakor.userMicroService.security.jwt.JwtUtils;
import com.amyojiakor.userMicroService.security.user.AppUserDetails;
import com.amyojiakor.userMicroService.services.AccountService;
import com.amyojiakor.userMicroService.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserServiceImplementation implements UserService {
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

        var existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent()){
            throw new Exception("You are already registered. Kindly login using this email");
        }

        User user = new User();
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Roles.USER);

        userRepository.save(user);

        AppUserDetails userDetails = new AppUserDetails(user);
        var jwt = jwtUtils.generateToken(userDetails);

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

            AppUserDetails userDetails = new AppUserDetails(user);

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

    private static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public User getCurrentUser() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("User not authenticated");
        }
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new Exception("User not found"));
    }


}
