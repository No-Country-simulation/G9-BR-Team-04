package com.g9team04.techmind.user.internal;

import com.g9team04.techmind.user.UserCreatedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Table(name = "tb_user")
public class UserEntity extends AbstractAggregateRoot<UserEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    protected UserEntity() {

    }
    public UserEntity(String email, String password) {
        this.email = email;
        this.password = password;
        registerEvent(new UserCreatedEvent(this.email));
    }

    String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
