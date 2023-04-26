package com.amyojiakor.userMicroService.models.payloads;


import com.amyojiakor.userMicroService.models.enums.AccountType;
import com.amyojiakor.userMicroService.models.enums.CurrencyCode;

import java.math.BigDecimal;

public record AccountResponse (String Email, String accountNumber, String accountName, AccountType accountType, CurrencyCode currencyCode, BigDecimal accountBalance) {


}
