package com.tarefas.controller;

import com.tarefas.services.UploadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UploadService uploadService;

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 400 quando o arquivo não for enviado")
    void deveRetornar400QuandoArquivoNaoForEnviado() throws Exception {

        // ACT
        var response = mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/uploads")
                        .with(csrf())
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(400, response.getStatus());
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 200 e a URL quando upload for bem-sucedido")
    void deveRetornar200QuandoUploadForBemSucedido() throws Exception {

        // ARRANGE
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagem.png",
                "image/png",
                "conteudo".getBytes()
        );

        given(uploadService.uploadImagem(any(MultipartFile.class)))
                .willReturn("https://bucket.s3.amazonaws.com/imagem.png");

        // ACT
        var response = mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/uploads")
                        .file(file)
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(200, response.getStatus());
        assertTrue(response.getContentAsString().contains("url"));
    }

    @Test
    @WithMockUser
    @DisplayName("Deve retornar 500 quando o service lançar exceção")
    void deveRetornar500QuandoServiceLancarExcecao() throws Exception {

        // ARRANGE
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagem.png",
                "image/png",
                "conteudo".getBytes()
        );

        given(uploadService.uploadImagem(any(MultipartFile.class)))
                .willThrow(new RuntimeException("Erro ao fazer upload"));

        // ACT
        var response = mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/uploads")
                        .file(file)
                        .with(csrf())
        ).andReturn().getResponse();

        // ASSERT
        assertEquals(500, response.getStatus());
    }

}