CREATE TABLE files
(
    id          BIGINT  PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255) NULL,
    size        INT          NOT NULL,
    upload_date VARCHAR(255) NULL,
    content longblob,
    user_id     BIGINT       NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);
