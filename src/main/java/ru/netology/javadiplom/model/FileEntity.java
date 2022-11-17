package ru.netology.javadiplom.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "files")
public class FileEntity implements Serializable {

    @Id
    private Long id;

    private String name;

    private int size;

    private String upload_date;

    private Byte[] content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public FileEntity(String name, int size, String upload_date, Byte[] content, User user) {
        this.name = name;
        this.size = size;
        this.upload_date = upload_date;
        this.content = content;
        this.user = user;
    }
}
