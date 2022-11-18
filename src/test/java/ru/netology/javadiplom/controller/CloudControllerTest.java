package ru.netology.javadiplom.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.netology.javadiplom.model.FileData;
import ru.netology.javadiplom.service.CloudStorageServiceImpl;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CloudControllerTest {

    MockMvc mockMvc;
    ObjectMapper objectMapper;
    CloudStorageServiceImpl cloudService;

    @BeforeEach
    void setUp() {
        cloudService = mock(CloudStorageServiceImpl.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new CloudController(cloudService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void delFile() throws Exception {
        mockMvc.perform(delete("/file")
                        .param("filename", "test.txt")
                        .header("auth-token", "Bearer auth-token #1"))
                .andExpect(status().isOk());
    }

    @Test
    void getFile() throws Exception {
        Resource resource = new UrlResource(Objects.requireNonNull(getClass().getClassLoader().getResource("test.txt")));
        Mockito.when(cloudService.getFile("test.txt")).thenReturn(resource);
        mockMvc.perform(get("/file")
                        .header("auth-token", "Bearer auth-token #1")
                        .param("filename", "test.txt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void getListOfFile() throws Exception {
        List<FileData> list = Collections.singletonList(new FileData("test.txt", 7777));
        Mockito.when(cloudService.getListOfFile(30)).thenReturn(list);
        mockMvc.perform(get("/list")
                        .header("auth-token", "Bearer auth-token #1")
                        .param("limit", "30"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @Test
    void editFile() throws Exception {
        String newFileName = "{\"filename\": \"789.txt\"}";
        mockMvc.perform(options("/file")
                        .param("filename", "test.txt")
                        .header("auth-token", "Bearer auth-token #1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFileName)))
                .andExpect(status().isOk());
    }
}