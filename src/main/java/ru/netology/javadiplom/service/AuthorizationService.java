package ru.netology.javadiplom.service;

import ru.netology.javadiplom.model.User;

import java.util.Map;

public interface AuthorizationService {

    Map<String, String> login(User user);

    String logout();
}
