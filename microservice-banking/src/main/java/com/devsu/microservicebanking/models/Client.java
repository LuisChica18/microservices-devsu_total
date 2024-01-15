package com.devsu.microservicebanking.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Client {

    private Long id;

    private String name;

    private String gender;

    private int age;

    private String dni;

    private String address;

    private String phone;
    private String password;

    private Boolean state;
}

