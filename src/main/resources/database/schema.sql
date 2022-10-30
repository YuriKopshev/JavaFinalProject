CREATE TABLE IF NOT EXISTS user
(
    id       int primary key auto_increment,
    login    varchar(255) not null,
    password varchar(255) not null,
    name     varchar(255),
    data_base_name   varchar(255) not null
);
DELETE FROM user;

INSERT INTO user (login, password, name, data_base_name) VALUES ('yura@gmail.com', '1111', 'Yura','yuraDataBase');
INSERT INTO user (login, password, name, data_base_name) VALUES ('ivan@gmail.com', '2222', 'Ivan', 'ivanDataBase');
INSERT INTO user (login, password, name, data_base_name) VALUES ('vlad@gmail.com', '3333', 'Vlad','vladDataBase');

CREATE TABLE IF NOT EXISTS yuraDataBase
(
    id          int primary key auto_increment,
    name        varchar(255) not null ,
    size        int check (size > 0),
    upload_date varchar(50),
    content     longblob not null
);

CREATE TABLE IF NOT EXISTS ivanDataBase
(
    id          int primary key auto_increment,
    name        varchar(255) not null ,
    size        int check (size > 0),
    upload_date varchar(50),
    content     longblob not null
);

CREATE TABLE IF NOT EXISTS vladDataBase
(
    id          int primary key auto_increment,
    name        varchar(255) not null ,
    size        int check (size > 0),
    upload_date varchar(50),
    content     longblob not null
);