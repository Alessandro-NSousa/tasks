package com.tarefas.services;

import com.tarefas.builder.TarefaDTOBuilder;
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
    @Test
    void whenTaskInformedThenItShouldBeCreated() {
        // dados
        var expectedtaskDTO = TarefaDTOBuilder.builder().build().toTarefa();
        var expectedTarefa = tarefaMapper.tarefaRequestDTOToTarefa(expectedtaskDTO);

        UUID userId = UUID.randomUUID();
        UUID colaboradorId = expectedtaskDTO.colaborador().getId(); // se vier do builder

        var usuarioLogado = new User();
        usuarioLogado.setId(userId);
        usuarioLogado.setNome("Usuário Logado");

        var colaborador = new User();
        colaborador.setId(colaboradorId != null ? colaboradorId : UUID.randomUUID());
        colaborador.setNome("Colaborador da Tarefa");

        //quando
        when(userService.getUsuarioLogado()).thenReturn(usuarioLogado);
        when(userRepository.findById(colaborador.getId())).thenReturn(Optional.of(colaborador));
        when(tarefaRepository.save(any())).thenReturn(expectedTarefa);

        // então
        var response = tarefaService.createTask(expectedtaskDTO);

        assertThat(response.titulo(), is(equalTo(expectedtaskDTO.titulo())));
        assertThat(response.descricao(), is(equalTo(expectedtaskDTO.descricao())));
    }

}