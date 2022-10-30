package ru.netology.javadiplom.service;

import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.javadiplom.exception.InputException;
import ru.netology.javadiplom.exception.UnauthorizedException;
import ru.netology.javadiplom.model.FileData;
import ru.netology.javadiplom.model.User;
import ru.netology.javadiplom.repository.CloudStorageRepository;
import ru.netology.javadiplom.repository.UserAccountRepository;
import ru.netology.javadiplom.security.JwtProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final CloudStorageRepository storageRepository;

    private final JwtProvider jwtProvider;

    public AuthorizationServiceImpl(CloudStorageRepository storageRepository, JwtProvider jwtProvider) {
        this.storageRepository = storageRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Map<String, String> login(User user) {
        Map<String, String> resultMap = new HashMap<>();
        if (user == null || user.getLogin().isEmpty() || user.getPassword().isEmpty()) {
            throw new UnauthorizedException("Invalid credentials");
        }
        User userData = storageRepository.login(user.getLogin());
        if (userData == null) {
            throw new UnauthorizedException("Invalid credentials");
        }
        resultMap.put("auth-token", jwtProvider.generateToken(userData.getLogin()));
        return resultMap;
    }

    @Override
    public String logout() {
        return "Successful logout";
    }
}
