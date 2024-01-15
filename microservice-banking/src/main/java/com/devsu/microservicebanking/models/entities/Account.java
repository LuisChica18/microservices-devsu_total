package com.devsu.microservicebanking.models.entities;

import com.devsu.microservicebanking.enums.AccountTypeEnum;
import com.devsu.microservicebanking.models.Client;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cuenta")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper=false)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "tipo_cuenta", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountTypeEnum accountType;

    @Column(name = "saldo_inicial", nullable = false)
    @Min(value = 1,message = "El saldo inicial debe ser mayor o igual 1")
    private BigDecimal initialBalance;

    @Column(name = "estado")
    private Boolean state;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cuenta_id")
    private List<AccountClient> accountClient;

    @Transient
    private Client client;
    
}
