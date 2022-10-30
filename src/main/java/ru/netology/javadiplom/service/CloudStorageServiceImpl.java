package ru.netology.javadiplom.service;

import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.javadiplom.exception.InputException;
import ru.netology.javadiplom.model.FileData;
import ru.netology.javadiplom.model.User;
import ru.netology.javadiplom.repository.CloudStorageRepository;
import ru.netology.javadiplom.repository.UserAccountRepository;
import ru.netology.javadiplom.security.JwtProvider;

import java.util.List;

@Service
public class CloudStorageServiceImpl implements CloudStorageService {

    private final CloudStorageRepository storageRepository;

    private final UserAccountRepository accountRepository;

    public CloudStorageServiceImpl(CloudStorageRepository storageRepository, UserAccountRepository accountRepository) {
        this.storageRepository = storageRepository;
        this.accountRepository = accountRepository;
    }

    public String getLoginFromUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    public User findByLogin(String login) {
        return accountRepository.findUserByLogin(login);
    }

    @Override
    public void loadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InputException("Error input data");
        }
        storageRepository.loadFile(file, findByLogin(getLoginFromUserDetails()).getData_base_name());
    }

    @Override
    public void deleteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new InputException("Error input data");
        }
        storageRepository.deleteFile(fileName, findByLogin(getLoginFromUserDetails()).getData_base_name());

    }

    @Override
    public Resource getFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new InputException("Error input data");
        }
        return storageRepository.getFile(fileName, findByLogin(getLoginFromUserDetails()).getData_base_name());

    }

    @Override
    public void editFile(String oldFileName, String newFileName) {
        if (oldFileName == null || newFileName == null || oldFileName.isEmpty() || newFileName.isEmpty()) {
            throw new InputException("Error input data");
        }
        storageRepository.editFile(oldFileName, newFileName, findByLogin(getLoginFromUserDetails()).getData_base_name());

    }

    @Override
    public List<FileData> getListOfFile(Integer limit) {
        if (limit <= 0 || limit > 30) {
            throw new InputException("Error input data");
        }
        return storageRepository.getListOfFile(limit, findByLogin(getLoginFromUserDetails()).getData_base_name());
    }
}
