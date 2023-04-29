package com.amyojiakor.userMicroService.models.payloads;


import com.amyojiakor.userMicroService.models.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionMessage(String accountNum, TransactionType transactionType, BigDecimal amount){
}
