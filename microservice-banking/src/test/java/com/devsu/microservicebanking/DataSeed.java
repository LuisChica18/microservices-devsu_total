package com.devsu.microservicebanking;

import com.devsu.microservicebanking.models.Client;
import com.devsu.microservicebanking.models.entities.Account;
import com.devsu.microservicebanking.enums.AccountTypeEnum;
import com.devsu.microservicebanking.models.entities.AccountClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataSeed {

    public static final Client client001 = new Client(1L, "Pepe", "Masculino", 12, "2323232323", "Calle 13", "59363636", "peperepe", true);

    static List<AccountClient> accountClients =  new ArrayList<>();


    public static final Account account001 = new Account(1L, "21221", AccountTypeEnum.AHORRO, new BigDecimal(2500), true, accountClients, client001);
//    public static final Account account002 = new Account(2L, "21222", AccountTypeEnum.AHORRO, new BigDecimal(3200), true, client001);
}
