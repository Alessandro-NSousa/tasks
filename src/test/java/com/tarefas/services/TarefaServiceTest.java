package com.tarefas.services;

import com.tarefas.builder.TarefaDTOBuilder;
import com.tarefas.builder.UserDTOBuilder;
import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.user.User;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
import com.tarefas.mapper.TarefaMapper;
import com.tarefas.repository.TarefaRepository;
import com.tarefas.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class) //para testes de unidade
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LogService logService;
    @Mock
    private UserService userService;
    private TarefaMapper tarefaMapper;
    private TarefaService tarefaService;

    @BeforeEach
    void setup() {
        tarefaMapper = Mappers.getMapper(TarefaMapper.class);
        tarefaService = new TarefaService(tarefaRepository, userRepository, tarefaMapper, logService, userService);
    }

    //quando a tarefa é informada, então ela deve ser criada
//    @Test
//    void whenTaskInformedThenItShouldBeCreated() {
//        // dados
//        var tarefaRequestDTO = TarefaDTOBuilder.builder().build().buildRequestDTO();
//        var tarefa = tarefaMapper.tarefaRequestDTOToTarefa(tarefaRequestDTO);
//
//        UUID userId = UUID.randomUUID();
//        UUID colaboradorId = tarefaRequestDTO.colaboradorId();
//
//        var usuarioLogado = new User();
//        usuarioLogado.setId(userId);
//        usuarioLogado.setNome("Usuário Logado");
//
//
//        //quando
//        when(userService.getUsuarioLogado()).thenReturn(usuarioLogado);
//        when(userRepository.findById(colaboradorId)).thenReturn(Optional.of(tarefa.getUsuario()));
//        when(tarefaRepository.save(any())).thenReturn(tarefa);
//
//        // então
//        var response = tarefaService.createTask(tarefaRequestDTO);
//
//        assertThat(response.titulo(), is(equalTo(tarefaRequestDTO.titulo())));
//        assertThat(response.descricao(), is(equalTo(tarefaRequestDTO.descricao())));
//        assertThat(response.status(), is(equalTo(tarefaRequestDTO.status())));
//        assertThat(response.colaborador(), is(equalTo(tarefaRequestDTO.colaboradorId())));
//    }

    @Test
    void whenTaskInformedThenItShouldBeCreated() {
        // Arrange
        var colaborador = UserDTOBuilder.builder()
                .nome("Colaborador da Tarefa")
                .build()
                .toUser();

        var usuarioLogado = UserDTOBuilder.builder()
                .nome("Usuário Logado")
                .build()
                .toUser();

        var tarefaBuilder = TarefaDTOBuilder.builder()
                .usuario(colaborador)
                .build();

        var tarefaRequestDTO = tarefaBuilder.buildRequestDTO();
        var tarefa = tarefaBuilder.buildEntity();

        // Mocks
        when(userService.getUsuarioLogado()).thenReturn(usuarioLogado);
        when(userRepository.findById(colaborador.getId())).thenReturn(Optional.of(colaborador));
        when(tarefaRepository.save(any())).thenReturn(tarefa);

        // Act
        var response = tarefaService.createTask(tarefaRequestDTO);

        // Assert
        assertThat(response.titulo(), is(tarefaRequestDTO.titulo()));
        assertThat(response.descricao(), is(tarefaRequestDTO.descricao()));
        assertThat(response.status(), is(tarefaRequestDTO.status()));
        assertThat(response.colaborador(), is(colaborador.getNome()));
    }

}