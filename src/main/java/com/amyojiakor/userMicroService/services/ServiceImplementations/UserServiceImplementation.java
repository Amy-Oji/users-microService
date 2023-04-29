package com.amyojiakor.userMicroService.services.ServiceImplementations;

import com.amyojiakor.userMicroService.models.entities.User;
import com.amyojiakor.userMicroService.models.entities.UserAccounts;
import com.amyojiakor.userMicroService.models.payloads.*;
import com.amyojiakor.userMicroService.respositories.UserAccountRepository;
import com.amyojiakor.userMicroService.respositories.UserRepository;
import com.amyojiakor.userMicroService.services.AuthService;
import com.amyojiakor.userMicroService.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final UserAccountRepository accountRepository;

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
    @Transactional
    @KafkaListener(topics = "${kafka.topic.account.creation}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "accountListenerContainerFactory" )
    public void consume(AccountResponse accountResponse) throws Exception {
       var user = userRepository.findByEmail(accountResponse.email()).orElseThrow(()-> new Exception("user with email: " + accountResponse.email() + " not found"));
        UserAccounts accounts = new UserAccounts();
        BeanUtils.copyProperties(accountResponse, accounts);
        accounts.setUser(user);
        System.out.println(accounts);
        System.out.println(accountResponse);
        accountRepository.save(accounts);
    }
    @Transactional
    @KafkaListener(topics = "${kafka.topic.account.transact}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "transactionListenerContainerFactory")
    public void consume(TransactionMessage transactionMessage) {
        System.out.println(transactionMessage.toString());
    }
}
