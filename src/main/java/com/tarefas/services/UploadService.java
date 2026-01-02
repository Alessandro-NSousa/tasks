package com.tarefas.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class UploadService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public UploadService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadImagem(MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("Arquivo vazio");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new RuntimeException("Apenas imagens são permitidas");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(file.getBytes())
            );

            return "https://" + bucket + ".s3.amazonaws.com/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer upload", e);
        }
    }
}
