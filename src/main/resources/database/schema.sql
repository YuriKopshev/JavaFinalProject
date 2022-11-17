#
# CREATE TABLE user
# (
#     id       BIGINT AUTO_INCREMENT   PRIMARY KEY  NOT NULL,
#     login    VARCHAR(255) NULL,
#     password VARCHAR(255) NULL,
#     name     VARCHAR(255) NULL
# );
#
# CREATE TABLE files
# (
#     id          BIGINT  PRIMARY KEY AUTO_INCREMENT NOT NULL,
#     name        VARCHAR(255) NULL,
#     size        INT          NOT NULL,
#     upload_date VARCHAR(255) NULL,
#     content longblob,
#     user_id     BIGINT       NULL,
#     FOREIGN KEY (user_id) REFERENCES user (id)
# );
#
#
# DELETE FROM user;
#
# INSERT INTO user (login, password, name) VALUES ('yura@gmail.com', '$2a$10$WQGM13xm5l.4XMDtPiyLf.6SdfTOtPHXWQP9z4pi53j5MfEkQUwYG', 'Yura'); -- password:1111
# INSERT INTO user (login, password, name) VALUES ('ivan@gmail.com', '$2a$10$xzqhyN7guu..JND4Kdg9vuEDnLyqSrw4YffHXDL3gFkzVuw1s1rri', 'Ivan'); -- password:2222
# INSERT INTO user (login, password, name) VALUES ('vlad@gmail.com', '$2a$10$jxJ71541yN0oLxcCg44LhubLyE.0HSsA4.RT0xJcXX0zVCQf.lGZK', 'Vlad'); -- password:3333