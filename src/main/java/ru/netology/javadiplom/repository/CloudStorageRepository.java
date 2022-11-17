package ru.netology.javadiplom.repository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.javadiplom.model.FileData;
import ru.netology.javadiplom.model.User;

import java.util.List;

public interface CloudStorageRepository {

    User login(String login, String password);

    void loadFile(MultipartFile file, User user);

    void deleteFile(String fileName, User user);

    Resource getFile(String fileName, User user);

    void editFile(String oldFileName, String newFileName, User user);

    List<FileData> getListOfFile(Integer limit, User user);
}

