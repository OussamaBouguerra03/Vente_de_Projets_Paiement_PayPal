package com.bouguerra.dev.mappers;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
@Component
public class MultipartFileMapper {

    // Method to convert MultipartFile to byte[]
    public static byte[] convertMultipartFileToByteArray(MultipartFile file) {
        try {
            return file != null ? file.getBytes() : null;
        } catch (IOException e) {
            throw new RuntimeException("Error converting MultipartFile to byte[]", e);
        }
    }
}
