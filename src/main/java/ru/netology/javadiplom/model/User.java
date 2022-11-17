package ru.netology.javadiplom.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    private Long id;

    private String login;

    private String password;

    private String name;


   @OneToMany
   private Set<FileEntity> fileEntity;

    public User(String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }
}
