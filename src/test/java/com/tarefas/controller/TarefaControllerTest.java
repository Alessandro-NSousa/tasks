package com.tarefas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tarefas.builder.TarefaDTOBuilder;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.List;

import static com.tarefas.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TarefaControllerTest {

    private static final String TASK_API_URL_PATH = "/api/tarefas";
    private MockMvc mockMvc;
    @Mock
    private TarefaService tarefaService;
    @InjectMocks
    private TarefaController tarefaController;
    TarefaMapper tarefaMapper = Mappers.getMapper(TarefaMapper.class);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tarefaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    //quando POST é chamado, então uma tarefa é criada
    @Test
    void whenPOSTIsCalledThenATaskIsCreated() throws Exception {
        // given
        var builder = TarefaDTOBuilder.builder().build();
        var requestDTO = builder.buildRequestDTO();
        var responseDTO = builder.buildResponseDTO();

        // when
        when(tarefaService.createTask(any(TarefaRequestDTO.class)))
                .thenReturn(responseDTO);

        // then
        mockMvc.perform(post(TASK_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo", is(responseDTO.titulo())))
                .andExpect(jsonPath("$.descricao", is(responseDTO.descricao())))
                .andExpect(jsonPath("$.status", is(responseDTO.status().toString())))
                .andExpect(jsonPath("$.colaborador", is(responseDTO.colaborador())));
    }

    //quando O GET é chamado, então o status ok é retornado
    @Test
    void whenGETIsCalledWhenOkStatusIsReturned() throws Exception {
        // given
        var builder = TarefaDTOBuilder.builder().build();
        var responseDTO = builder.buildResponseDTO();

        Page<TarefaResponseDTO> tarefaPage = new PageImpl<>(List.of(responseDTO), PageRequest.of(0,10),1);

        // when
        when(tarefaService.getAllTasks(any(Pageable.class))).thenReturn(tarefaPage);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(TASK_API_URL_PATH)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(1)))
                .andExpect(jsonPath("$.content[0].titulo", is(responseDTO.titulo())))
                .andExpect(jsonPath("$.content[0].descricao", is(responseDTO.descricao())))
                .andExpect(jsonPath("$.content[0].status", is(responseDTO.status().name())))
                .andExpect(jsonPath("$.content[0].colaborador", is(responseDTO.colaborador())));
    }

    @Test
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        var tarefa = TarefaDTOBuilder.builder().build().buildEntity();
        var esperadaResponseDTO = tarefaMapper.tarefaToTarefaResponseDTO(tarefa);

        when(tarefaService.getByTask(esperadaResponseDTO.id())).thenReturn(esperadaResponseDTO);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(TASK_API_URL_PATH + "/" + esperadaResponseDTO.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is(esperadaResponseDTO.titulo())))
                .andExpect(jsonPath("$.descricao", is(esperadaResponseDTO.descricao())))
                .andExpect(jsonPath("$.status", is(esperadaResponseDTO.status().toString())));
    }

    @Test
    void whenGETIsCalledWithValidIdOfUserThenOkStatusIsReturned() throws Exception {
        var tarefa = TarefaDTOBuilder.builder().build().buildEntity();
        var esperadaResponseDTO = tarefaMapper.tarefaToTarefaResponseDTO(tarefa);

        Page<TarefaResponseDTO> tarefaPage = new PageImpl<>(List.of(esperadaResponseDTO), PageRequest.of(0,10),1);

        when(tarefaService.getByUser(eq(tarefa.getUsuario().getId()),any(Pageable.class))).thenReturn(tarefaPage);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(TASK_API_URL_PATH + "/user/" + tarefa.getUsuario().getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titulo", is(esperadaResponseDTO.titulo())))
                .andExpect(jsonPath("$.content[0].descricao", is(esperadaResponseDTO.descricao())))
                .andExpect(jsonPath("$.content[0].status", is(esperadaResponseDTO.status().toString())));
    }

}