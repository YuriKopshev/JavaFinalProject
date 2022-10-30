package ru.netology.javadiplom.repository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.javadiplom.model.FileData;
import ru.netology.javadiplom.model.User;

import java.util.List;

public interface CloudStorageRepository {

    User login(String login);

    void loadFile(MultipartFile file, String dataBaseName);

    void deleteFile(String fileName, String dataBaseName);

    Resource getFile(String fileName, String dataBaseName);

    void editFile(String oldFileName, String newFileName, String dataBaseName);

    List<FileData> getListOfFile(Integer limit, String dataBaseName);
}

