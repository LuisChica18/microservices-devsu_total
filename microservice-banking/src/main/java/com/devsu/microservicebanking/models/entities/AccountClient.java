package com.devsu.microservicebanking.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cuentas_clientes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="cliente_id", unique = true)
    private Long clientId;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AccountClient)) {
            return false;
        }
        AccountClient o = (AccountClient) obj;
        return this.clientId != null && this.clientId.equals(o.clientId);
    }
}
