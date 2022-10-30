package ru.netology.javadiplom.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.netology.javadiplom.model.User;
import ru.netology.javadiplom.service.CloudStorageServiceImpl;

@Component
public class AccountUserDetailsService implements UserDetailsService {

    private final CloudStorageServiceImpl storageService;

    public AccountUserDetailsService(CloudStorageServiceImpl storageService) {
        this.storageService = storageService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = storageService.findByLogin(login);
        return AccountUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
