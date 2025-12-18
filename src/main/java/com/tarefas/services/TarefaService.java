package com.tarefas.services;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.user.User;
import com.tarefas.dto.*;
import com.tarefas.mapper.TarefaMapper;
import com.tarefas.repository.TarefaRepository;
import com.tarefas.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TarefaService {


    private TarefaRepository repository;

    private UserRepository userRepository;

    private TarefaMapper mapper;

    private LogService logService;

    private UserService userService;

    public TarefaResponseDTO createTask(TarefaRequestDTO data) {

        var usuarioLogado = userService.getUsuarioLogado();

        Tarefa tarefa = mapper.tarefaRequestDTOToTarefa(data);

        User usuario = userRepository.findById(data.colaboradorId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        tarefa.setUsuario(usuario);

        var newTask = repository.save(tarefa);

        //cadastrar o log da tarefa
        var log = new LogRequestDTO("O usuário " + usuarioLogado.getNome() + ", de Id: " + usuarioLogado.getId()
                +", cadastrou uma nova tarefa para o usuário " + usuario.getNome()
                , Tarefa.class.getSimpleName(),usuarioLogado.getId());
        logService.RegistrarLog(log);

        return mapper.tarefaToTarefaResponseDTO(newTask);
    }

    public Page<TarefaResponseDTO> getAllTasks(Pageable pageable) {

        return repository.findAllByAtivoTrue(pageable).map(mapper::tarefaToTarefaResponseDTO);
    }

    public Page<TarefaResponseDTO> getTasksByStatus(String statusString, Pageable pageable) {
        Status status = Status.fromString(statusString);
        return repository.findByStatusAndAtivoTrue(status, pageable).map(mapper::tarefaToTarefaResponseDTO);
    }

    public TarefaResponseDTO getByTask(UUID taskId) {

        Tarefa tarefa = repository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));

        return mapper.tarefaToTarefaResponseDTO(tarefa);
    }

    public Page<TarefaResponseDTO> getByUser(UUID userId, Pageable pageable) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário nao encontrado"));

        return repository.findAllByUsuario(user, pageable).map(mapper::tarefaToTarefaResponseDTO);
    }

    public TarefaResponsePutDTO atualizarTarefa(UUID id, TarefaPutRequestDTO dados) {
        var tarefa = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        mapper.updateTarefaFromDTO(dados, tarefa, userRepository);

        return mapper.tarefaToTarefaResponsePutDTO(repository.save(tarefa));
    }

    public void deleteTask(UUID id) {
        var tarefa = repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada ou já excluída"));

        tarefa.setAtivo(false);
        repository.save(tarefa);

        var usuarioLogado = userService.getUsuarioLogado();
        var log = new LogRequestDTO(
                "O usuário " + usuarioLogado.getNome() + " (ID: " + usuarioLogado.getId() +
                 ") ecluiu a tarefa '" + tarefa.getTitulo() + "' .",
                Tarefa.class.getSimpleName(),
                usuarioLogado.getId()
        );
        logService.RegistrarLog(log);
    }
}
