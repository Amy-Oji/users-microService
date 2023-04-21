package com.amyojiakor.userMicroService.models.entities;

import com.amyojiakor.userMicroService.models.enums.AccountType;
import com.amyojiakor.userMicroService.models.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class UserAccounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    @Enumerated(value = EnumType.STRING)
    private AccountType accountType;

    @Enumerated(value = EnumType.STRING)
    private CurrencyCode currencyCode;

    private BigDecimal accountBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
