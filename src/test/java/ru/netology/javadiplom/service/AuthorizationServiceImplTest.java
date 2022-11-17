package ru.netology.javadiplom.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.netology.javadiplom.exception.UnauthorizedException;
import ru.netology.javadiplom.model.User;
import ru.netology.javadiplom.repository.CloudStorageRepositoryImpl;
import ru.netology.javadiplom.security.JwtProvider;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


class AuthorizationServiceImplTest {


    @Mock
    CloudStorageRepositoryImpl repository;

    @InjectMocks
    AuthorizationServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginTestWithUserNull() {
        assertThrows(UnauthorizedException.class, () -> service.login(null));
    }

    @Test
    void loginTestWithUserEmptyPassword() {
        User user = new User("yuri@mail.ru", " ", "yuri");
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> service.login(user));
        assertTrue(exception.getMessage().contains("Invalid credentials"));
    }

    @Test
    void loginTestWithUserEmptyLogin() {
        User user = new User(" ", "2222", "yuri");
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> service.login(user));
        assertTrue(exception.getMessage().contains("Invalid credentials"));
    }

    @Test
    void logout() {
        String expected = "Successful logout";
        assertEquals(expected, service.logout());
    }

    @Test
    void successLogin() {
        Map<String, String> expect = new ConcurrentHashMap<>();
        expect.put("auth-token", "token_value");
        User user = new User("yuri@mail.ru", "2222", "yuri");
        JwtProvider jwtProvider = Mockito.mock(JwtProvider.class);
        CloudStorageRepositoryImpl repo = Mockito.mock(CloudStorageRepositoryImpl.class);
        AuthorizationServiceImpl authorizationService = new AuthorizationServiceImpl(repo, jwtProvider);
        when(repo.login("yuri@mail.ru", "2222")).thenReturn(user);
        when(jwtProvider.generateToken(user.getLogin())).thenReturn("token_value");
        Map<String, String> actual = authorizationService.login(user);
        assertEquals(expect, actual);
    }
}