package com.amyojiakor.userMicroService.services.ServiceImplementations;

import com.amyojiakor.userMicroService.models.entities.User;
import com.amyojiakor.userMicroService.models.entities.UserAccounts;
import com.amyojiakor.userMicroService.models.enums.TransactionStatus;
import com.amyojiakor.userMicroService.models.enums.TransactionType;
import com.amyojiakor.userMicroService.models.payloads.*;
import com.amyojiakor.userMicroService.respositories.UserAccountRepository;
import com.amyojiakor.userMicroService.respositories.UserRepository;
import com.amyojiakor.userMicroService.services.AuthenticationService;
import com.amyojiakor.userMicroService.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final UserAccountRepository accountRepository;

    private final AuthenticationService authenticationService;

    private final KafkaTemplate<String, TransactionMessageResponse> kafkaTemplate;

    private final String balanceUpdateTopic;

   @Autowired
    public UserServiceImplementation(UserRepository userRepository, UserAccountRepository accountRepository, AuthenticationService authenticationService, KafkaTemplate<String, TransactionMessageResponse> kafkaTemplate, @Value("${kafka.topic.transaction.balance-update}") String balanceUpdateTopic) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.authenticationService = authenticationService;
        this.kafkaTemplate = kafkaTemplate;
        this.balanceUpdateTopic = balanceUpdateTopic;
    }

    public UserDetailsResponse getUserDetails() throws Exception {
        User user = authenticationService.getCurrentUser();
        return new UserDetailsResponse(user.getFirstName(), user.getLastName(), user.getEmail(),  user.getAccounts());
    }

    @Override
    public UpdateUserDetailsDto updateUserDetails(UpdateUserDetailsDto updateUserDetailsDto) throws Exception {
        User user = authenticationService.getCurrentUser();
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
    @KafkaListener(topics = "${kafka.topic.transaction.transact}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "transactionListenerContainerFactory")
    public void consume(TransactionMessage transactionMessage) throws Exception {
        System.out.println(transactionMessage+ " === transactionMessage");
        TransactionMessageResponse transactionMessageResponse = processTransaction(transactionMessage);
        kafkaTemplate.send(balanceUpdateTopic, transactionMessageResponse);
        System.out.println(transactionMessageResponse);
    }

    private TransactionMessageResponse processTransaction(TransactionMessage transactionMessage) throws Exception {
        TransactionMessageResponse transactionMessageResponse = new TransactionMessageResponse();
        var account = accountRepository.findByAccountNumber(transactionMessage.accountNum()).orElseThrow();
        var balance = account.getAccountBalance();
        if (transactionMessage.transactionType() == TransactionType.CREDIT) {
            account.setAccountBalance(balance.add(transactionMessage.amount()));
        } else {
            if (transactionMessage.amount().compareTo(balance) > 0) {
                transactionMessageResponse.setTransactionStatus(TransactionStatus.FAILED);
                transactionMessageResponse.setErrorMessage("Insufficient Funds");
                throw new Exception("Insufficient Funds");
            }
            account.setAccountBalance(balance.subtract(transactionMessage.amount()));
        }
        transactionMessageResponse.setTransactionStatus(TransactionStatus.COMPLETED);
        transactionMessageResponse.setErrorMessage(null);
        accountRepository.save(account);
        return transactionMessageResponse;
    }
}
