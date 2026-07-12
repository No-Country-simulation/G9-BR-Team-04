package com.g9team04.techmind.user.internal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity

public class UserEntity extends AbstractAggregateRoot<UserEntity> {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    private String email;
    private String password;

    protected UserEntity() {

    }
    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    String getPassword() {
        return password;
    }

    void setEmail(String email) {
        this.email = email;
    }

    void setPassword(String password) {
        this.password = password;
    }

}
