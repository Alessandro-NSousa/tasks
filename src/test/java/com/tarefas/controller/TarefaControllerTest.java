package com.tarefas.controller;

import com.tarefas.builder.TarefaDTOBuilder;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.mapper.TarefaMapper;
import com.tarefas.services.TarefaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.UUID;

import static com.tarefas.utils.JsonConvertionUtils.asJsonString;
import static org.checkerframework.checker.units.qual.Prefix.one;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TarefaControllerTest {

    private static final String TASK_API_URL_PATH = "/api/tarefas";
    private static final UUID VALID_TASK_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID INVALID_TASK_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private MockMvc mockMvc;

    @Mock
    private TarefaService tarefaService;
    @InjectMocks
    private TarefaController tarefaController;

    @Spy
    private TarefaMapper tarefaMapper = Mappers.getMapper(TarefaMapper.class);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tarefaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    //quando POST é chamado, então uma tarefa é criada
//    @Test
//    void whenPOSTIsCalledThenATaskIsCreated() throws Exception {
//        // given
//        var tarefaRequestDTO = TarefaDTOBuilder.builder().build().buildRequestDTO();
//        var expectedTarefa = tarefaMapper.tarefaRequestDTOToTarefa(tarefaRequestDTO);
//        var expectedTarefaDTO = tarefaMapper.TarefaToTarefaResponseDTO(expectedTarefa);
//
//
//
//        // when
//        when(tarefaService.createTask(tarefaRequestDTO)).thenReturn(expectedTarefaDTO);
//
//        // then
//        mockMvc.perform(post(TASK_API_URL_PATH)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(tarefaRequestDTO)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.titulo", is(expectedTarefaDTO.titulo())))
//                .andExpect(jsonPath("$.descricao", is(expectedTarefaDTO.descricao())))
//                .andExpect(jsonPath("$.status", is(expectedTarefaDTO.status())));
//    }

    @Test
    void whenPOSTIsCalledThenATaskIsCreated() throws Exception {
        // given
        var tarefaRequestDTO = TarefaDTOBuilder.builder().build().buildRequestDTO(); // entrada da requisição
        var tarefaDomain = tarefaMapper.tarefaRequestDTOToTarefa(tarefaRequestDTO); // converte para entidade
        var tarefaResponseDTO = tarefaMapper.tarefaToTarefaResponseDTO(tarefaDomain); // resultado esperado

        // when
        when(tarefaService.createTask(any(TarefaRequestDTO.class))).thenReturn(tarefaResponseDTO);

        // then
        mockMvc.perform(post(TASK_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tarefaRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is(tarefaResponseDTO.titulo())))
                .andExpect(jsonPath("$.descricao", is(tarefaResponseDTO.descricao())))
                .andExpect(jsonPath("$.status", is(tarefaResponseDTO.status())));
    }



}