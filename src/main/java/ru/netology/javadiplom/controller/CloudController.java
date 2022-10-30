package ru.netology.javadiplom.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.javadiplom.model.FileData;
import ru.netology.javadiplom.service.CloudStorageServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/")
public class CloudController {
    private final CloudStorageServiceImpl service;

    public CloudController(CloudStorageServiceImpl service) {
        this.service = service;
    }

    @PostMapping("file")
    public ResponseEntity<String> loadFile(@RequestParam("file") MultipartFile file, @RequestHeader("auth-token") String token) {
        service.loadFile(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("file")
    public ResponseEntity<String> deleteFile(@RequestParam("filename") String fileName, @RequestHeader("auth-token") String token) {
        service.deleteFile(fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("file")
    public ResponseEntity<Resource> getFile(@RequestParam("filename") String fileName, @RequestHeader("auth-token") String token) {
        return new ResponseEntity<>(service.getFile(fileName), HttpStatus.OK);
    }

    @PutMapping("file")
    public ResponseEntity<String> editFile(@RequestParam("filename") String oldFileName, @RequestHeader("auth-token") String token, @RequestBody FileData newFileName) {
        service.editFile(oldFileName,newFileName.getFileName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("list")
    public ResponseEntity<List<FileData>> getFileList(@RequestParam("limit") Integer limit, @RequestHeader("auth-token") String token) {
        return ResponseEntity.status(200).body(service.getListOfFile(limit));
    }
}
