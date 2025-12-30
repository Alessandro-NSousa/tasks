package com.tarefas.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UploadServiceTest {

    @InjectMocks
    private UploadService service;

    @Mock
    private S3Client s3Client;

    @Mock
    private MultipartFile file;

    @Captor
    private ArgumentCaptor<PutObjectRequest> requestCaptor;

    private static final String BUCKET = "meu-bucket-teste";

    @BeforeEach
    void setup() {
        // Injeta o valor do @Value manualmente
        ReflectionTestUtils.setField(service, "bucket", BUCKET);
    }

    @Test
    @DisplayName("Deve fazer upload da imagem com sucesso")
    void deveFazerUploadComSucesso() throws Exception {

        // Arrange
        given(file.isEmpty()).willReturn(false);
        given(file.getContentType()).willReturn("image/png");
        given(file.getOriginalFilename()).willReturn("foto.png");
        given(file.getBytes()).willReturn("conteudo".getBytes());

        // Act
        String url = service.uploadImagem(file);

        // Then
        then(s3Client).should()
                .putObject(requestCaptor.capture(), any(RequestBody.class));

        PutObjectRequest request = requestCaptor.getValue();

        assertEquals(BUCKET, request.bucket());
        assertTrue(request.key().contains("foto.png"));
        assertEquals("image/png", request.contentType());

        assertTrue(url.startsWith("https://" + BUCKET + ".s3.amazonaws.com/"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o arquivo estiver vazio")
    void deveLancarExcecaoQuandoArquivoVazio() {

        // Arrange
        given(file.isEmpty()).willReturn(true);

        // Act + Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.uploadImagem(file)
        );

        assertEquals("Arquivo vazio", exception.getMessage());
        then(s3Client).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Deve lançar exceção quando o arquivo não for uma imagem")
    void deveLancarExcecaoQuandoNaoForImagem() {

        // Arrange
        given(file.isEmpty()).willReturn(false);
        given(file.getContentType()).willReturn("application/pdf");

        // Act + Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.uploadImagem(file)
        );

        assertEquals("Apenas imagens são permitidas", exception.getMessage());
        then(s3Client).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro ao ler o arquivo")
    void deveLancarExcecaoQuandoOcorrerErroDeLeitura() throws Exception {

        // Arrange
        given(file.isEmpty()).willReturn(false);
        given(file.getContentType()).willReturn("image/jpeg");
        given(file.getOriginalFilename()).willReturn("foto.jpg");
        given(file.getBytes()).willThrow(new IOException("Erro de IO"));

        // Act + Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.uploadImagem(file)
        );

        assertEquals("Erro ao fazer upload", exception.getMessage());
        assertNotNull(exception.getCause());

        then(s3Client).shouldHaveNoInteractions();
    }

}