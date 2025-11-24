package com.tarefas.services;

import com.tarefas.builder.TarefaDTOBuilder;
import com.tarefas.builder.UserDTOBuilder;
import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.dto.TarefaResponseDTO;
import com.tarefas.mapper.TarefaMapper;
import com.tarefas.repository.TarefaRepository;
import com.tarefas.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

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
    @Mock
    private TarefaMapper tarefaMapper;

    @InjectMocks
    private TarefaService tarefaService;

    //quando a tarefa é informada, então ela deve ser criada
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
        var tarefaResponseDTO = tarefaBuilder.buildResponseDTO();

        // Mocks
        when(tarefaMapper.tarefaRequestDTOToTarefa(tarefaRequestDTO)).thenReturn(tarefa);
        when(userService.getUsuarioLogado()).thenReturn(usuarioLogado);
        when(userRepository.findById(colaborador.getId())).thenReturn(Optional.of(colaborador));
        when(tarefaRepository.save(any())).thenReturn(tarefa);
        when(tarefaMapper.tarefaToTarefaResponseDTO(tarefa)).thenReturn(tarefaResponseDTO);

        // Act
        var response = tarefaService.createTask(tarefaRequestDTO);

        // Assert
        assertThat(response.titulo(), is(tarefaRequestDTO.titulo()));
        assertThat(response.descricao(), is(tarefaRequestDTO.descricao()));
        assertThat(response.status(), is(tarefaRequestDTO.status()));
        assertThat(response.colaborador(), is(colaborador.getNome()));
    }

    //todo: implementar teste de erro ao cadastrar uma nova tarefa

    //quando a Lista de tarefas for chamada, então retorne uma Lista de tarefas
    @Test
    void whenListTasksIsCalledThenReturnAListOfTasks() {
        // given

        var colaborador = UserDTOBuilder.builder()
                .nome("Colaborador da Tarefa")
                .build()
                .toUser();

        var tarefaBuilder = TarefaDTOBuilder.builder()
                .usuario(colaborador)
                .build();

        var tarefa = tarefaBuilder.buildEntity();
        var tarefaResponseDTO = tarefaBuilder.buildResponseDTO();

        Pageable pageable = PageRequest.of(0, 10); // página 0, 10 itens por página
        Page<Tarefa> tarefaPage = new PageImpl<>(List.of(tarefa));

        //when
        when(tarefaRepository.findAllByAtivoTrue(pageable)).thenReturn(tarefaPage);
        when(tarefaMapper.tarefaToTarefaResponseDTO(tarefa)).thenReturn(tarefaResponseDTO);

        //then
        Page<TarefaResponseDTO> foundListTasksDTO = tarefaService.getAllTasks(pageable);

        assertThat(foundListTasksDTO.getContent(), is(not(empty()))); //verifica se não retorna vazio
        assertThat(foundListTasksDTO.getContent().get(0), is(equalTo(tarefaResponseDTO))); //compara
    }

    @Test
    void whenValidTaskIdIsGivenThenReturnATask(){
        var tarefa = TarefaDTOBuilder.builder().build().buildEntity();
        var esperadaResponseDTO = tarefaMapper.tarefaToTarefaResponseDTO(tarefa);

        when(tarefaRepository.findById(tarefa.getId())).thenReturn(Optional.of(tarefa));
        when(tarefaMapper.tarefaToTarefaResponseDTO(tarefa)).thenReturn(esperadaResponseDTO);

        TarefaResponseDTO responseDTO = tarefaService.getByTask(tarefa.getId());

        assertThat(responseDTO, is(equalTo(esperadaResponseDTO)));
    }

    @Test
    void whenValidUserIdIsGivenThenReturnATask(){
        var tarefa = TarefaDTOBuilder.builder().build().buildEntity();
        var esperadaResponseDTO = tarefaMapper.tarefaToTarefaResponseDTO(tarefa);

        Pageable pageable = PageRequest.of(0, 10); // página 0, 10 itens por página
        Page<Tarefa> tarefaPage = new PageImpl<>(List.of(tarefa));

        when(userRepository.findById(tarefa.getUsuario().getId())).thenReturn(Optional.of(tarefa.getUsuario()));
        when(tarefaRepository.findAllByUsuario(tarefa.getUsuario(), pageable)).thenReturn(tarefaPage);
        when(tarefaMapper.tarefaToTarefaResponseDTO(tarefa)).thenReturn(esperadaResponseDTO);

        Page<TarefaResponseDTO> responseDTO = tarefaService.getByUser(tarefa.getUsuario().getId(), pageable );

        assertThat(responseDTO.getContent().get(0), is(equalTo(esperadaResponseDTO)));
    }

    @Test
    void WhenATaskStatusIsProvidedReturnATaskList() {

        var tarefa = TarefaDTOBuilder.builder().build().buildEntity();
        var esperadaResponseDTO = tarefaMapper.tarefaToTarefaResponseDTO(tarefa);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Tarefa> tarefaPage = new PageImpl<>(List.of(tarefa), pageable, 1);

        when(tarefaRepository.findByStatusAndAtivoTrue(tarefa.getStatus(), pageable))
                .thenReturn(tarefaPage);

        when(tarefaMapper.tarefaToTarefaResponseDTO(tarefa))
                .thenReturn(esperadaResponseDTO);

        Page<TarefaResponseDTO> responseDTO = tarefaService.getTasksByStatus(
                tarefa.getStatus().toString(),
                pageable
        );
        // Verifica conteúdo
        assertThat(responseDTO.getContent().get(0), is(equalTo(esperadaResponseDTO)));
        // Verifica metadados da Page
        assertThat(responseDTO.getTotalElements(), is(1L));
        assertThat(responseDTO.getContent().size(), is(1));
        // Verifica se o repository foi chamado corretamente
        verify(tarefaRepository).findByStatusAndAtivoTrue(tarefa.getStatus(), pageable);
    }
}