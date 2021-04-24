package com.bingo.pojo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadRequest {
    private String text;
    private byte[] photo;
}
