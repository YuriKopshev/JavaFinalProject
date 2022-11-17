package ru.netology.javadiplom.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.javadiplom.exception.InputException;
import ru.netology.javadiplom.exception.UnauthorizedException;
import ru.netology.javadiplom.model.User;
import ru.netology.javadiplom.repository.CloudStorageRepositoryImpl;
import ru.netology.javadiplom.security.JwtProvider;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CloudStorageServiceImplTest {

    @Mock
    CloudStorageRepositoryImpl repository;

    @InjectMocks
    CloudStorageServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFile() {
        MultipartFile file = null;
        InputException exception = assertThrows(InputException.class, () -> service.loadFile(file));
        assertTrue(exception.getMessage().contains("Error input data"));
    }

    @Test
    void delFile() {
        String fileName = "";
        InputException exception = assertThrows(InputException.class, () -> service.deleteFile(fileName));
        assertTrue(exception.getMessage().contains("Error input data"));
    }

    @Test
    void getFileWhenFileNameIsNull() {
        String fileName = null;
        InputException exception = assertThrows(InputException.class, () -> service.getFile(fileName));
        assertTrue(exception.getMessage().contains("Error input data"));
    }

    @Test
    void editFile() {
        String oldFileName = null, newFileName = null;
        InputException exception = assertThrows(InputException.class, () -> service.editFile(oldFileName, newFileName));
        assertTrue(exception.getMessage().contains("Error input data"));
    }
}
