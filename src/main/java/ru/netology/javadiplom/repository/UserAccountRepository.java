package ru.netology.javadiplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netology.javadiplom.model.User;

public interface UserAccountRepository extends JpaRepository<User,Long> {

    User findUserByLogin(String login);
}
