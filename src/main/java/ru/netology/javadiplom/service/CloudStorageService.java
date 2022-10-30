package ru.netology.javadiplom.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.javadiplom.model.FileData;

import java.util.List;

public interface CloudStorageService {

    void loadFile(MultipartFile file);

    void deleteFile(String fileName);

    Resource getFile(String fileName);

    void editFile(String oldFileName, String newFileName);

    List<FileData> getListOfFile(Integer limit);
}
