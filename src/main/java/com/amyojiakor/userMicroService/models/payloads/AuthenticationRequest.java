package com.amyojiakor.userMicroService.models.payloads;


public record AuthenticationRequest (String email, String password) {
}
