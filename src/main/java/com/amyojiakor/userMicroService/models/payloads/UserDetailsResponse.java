package com.amyojiakor.userMicroService.models.payloads;

import com.amyojiakor.userMicroService.models.entities.UserAccounts;

import java.util.List;

public record UserDetailsResponse(String firstName, String lastName, String email, List<UserAccounts> userAccounts){
}
