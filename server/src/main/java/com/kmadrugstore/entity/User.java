package com.kmadrugstore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String email;
    private int bonuses;
    private String password;
    @ManyToOne
    @JoinColumn(name = "id_role", nullable = false)
    private Role role;

    public User(final String name, final String surname, final String email,
                final Role role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
    }

    public User(final int id) {
        this.id = id;
    }
}
