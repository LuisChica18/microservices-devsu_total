package com.devsu.microservicebanking.models;

import com.devsu.microservicebanking.enums.AccountTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Report {

    private String accountNumber;
    private BigDecimal previousBalance;
    private BigDecimal movement;
    private BigDecimal balance;
    private LocalDateTime dateTransaction;
    private String client;
    private AccountTypeEnum movementType;
    private boolean status;



}
