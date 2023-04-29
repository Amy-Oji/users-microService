package com.amyojiakor.userMicroService.models.payloads;

import com.amyojiakor.userMicroService.models.enums.TransactionStatus;
import lombok.Data;

@Data
public class TransactionMessageResponse {
   private TransactionStatus transactionStatus;
   private String errorMessage;
}
