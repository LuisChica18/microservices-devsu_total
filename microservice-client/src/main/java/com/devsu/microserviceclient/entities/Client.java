package com.devsu.microserviceclient.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "cliente")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class Client extends Person {

    @Column(name = "contrasena", nullable = false)
    @NotNull(message = "La contraseña es requerida")
    private String password;

    @Column(name = "estado")
    private Boolean state;

}
