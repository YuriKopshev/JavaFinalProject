package ru.netology.javadiplom.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class User {

    @Id
    private long id;

    private String login;

    private String password;

    private String name;

    private String data_base_name;

    public User(String login, String password, String name, String data_base_name) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.data_base_name = data_base_name;
    }
}
