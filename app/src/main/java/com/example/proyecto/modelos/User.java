package com.example.proyecto.modelos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String name;
    private String email;
    private long phone;

    public User() {
    }

    public User(String name, String email, long phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }



}
