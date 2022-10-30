package ru.netology.javadiplom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.javadiplom.model.User;
import ru.netology.javadiplom.service.AuthorizationServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("/")
public class AuthorizationController {

    private final AuthorizationServiceImpl service;

    public AuthorizationController(AuthorizationServiceImpl service) {
        this.service = service;
    }

    @PostMapping("login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        return ResponseEntity.ok(service.login(user));
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(@RequestHeader("auth-token") String token) {
        return new ResponseEntity<>(service.logout(), HttpStatus.OK);
    }

}
