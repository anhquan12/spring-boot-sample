package com.example.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;
import rx.Observable;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    public ResponseEntity<List<String>> saveImages(List<MultipartFile> files, List<String> fileNames) {
        return new ResponseEntity<>(saveImagesImpl(files, fileNames), HttpStatus.OK);
    }

    private List<String> saveImagesImpl(List<MultipartFile> files, List<String> fileNames) throws ResourceAccessException {
        if(fileNames.size() != files.size()) {
//            throw new ResourceException("{file_number.not_match}");
            throw new RuntimeException("file_number_not_match");
        }
        List<String> fileNameWithExtension = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String fileExtension = "." + FilenameUtils.getExtension(file.getOriginalFilename());
            String filename = fileNames.get(i) + fileExtension;
            try {
                byte[] bytes = file.getBytes();
                fileNameWithExtension.add(filename);
                Path filePath = Paths.get("/Users/anhquan12/Desktop/var/upload/", "origin", filename);
                File uploadFile = new File(filePath.toString());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(uploadFile));
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("{internal.server.error}");
            }
        }
        return fileNameWithExtension;
    }

    public ResponseEntity<?> getImage(String size, String filename) {
        Path filePath;
        filePath = Paths.get("/Users/anhquan12/Desktop/var/upload/", "origin", size, filename);
        if(!Files.exists(filePath)) {
            filePath = Paths.get("/Users/anhquan12/Desktop/var/upload/", "origin", filename);
        }
        if(Files.exists(filePath)) {
            try {
                byte[] image = FileUtils.readFileToByteArray(new File(filePath.toString()));
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG);
                headers.setContentType(MediaType.IMAGE_JPEG);
                headers.setContentLength(image.length);
                return ResponseEntity.ok().headers(headers).body(image);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("file not found");
            }
        } else {
            return ResponseEntity.badRequest().body("file not found");
        }
    }

}
