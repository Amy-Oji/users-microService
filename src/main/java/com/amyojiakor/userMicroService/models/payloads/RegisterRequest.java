package com.amyojiakor.userMicroService.models.payloads;

public record RegisterRequest(String firstName, String lastName, String email, String password) {

}
