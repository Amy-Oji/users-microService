package com.amyojiakor.userMicroService.models.payloads;

public record UpdatePasswordDto (String currentPassword, String newPassword) {
}
