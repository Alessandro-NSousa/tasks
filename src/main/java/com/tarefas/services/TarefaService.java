package com.tarefas.services;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.user.User;
import com.tarefas.dto.LogRequestDTO;
import com.tarefas.dto.TarefaPutRequestDTO;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
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

        User usuario = userRepository.findById(data.colaborador().getId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        tarefa.setUsuario(usuario);

        var newTask = repository.save(tarefa);

        //cadastrar o log da tarefa
        var log = new LogRequestDTO("O usuário " + usuarioLogado.getNome() + ", de Id: " + usuarioLogado.getId()
                +", cadastrou uma nova tarefa para o usuário " + usuario.getNome()
                , Tarefa.class.getSimpleName(),usuarioLogado.getId());
        logService.RegistrarLog(log);

        return mapper.TarefaToTarefaResponseDTO(newTask);
    }

    public Page<TarefaResponseDTO> getAllTasks(Pageable pageable) {

        return repository.findAll(pageable).map(mapper::TarefaToTarefaResponseDTO);
    }

    public TarefaResponseDTO getByTask(UUID taskId) {

        Tarefa tarefa = repository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));

        return mapper.TarefaToTarefaResponseDTO(tarefa);
    }

    public Page<TarefaResponseDTO> getByUser(UUID userId, Pageable pageable) {

        var user = userRepository.findById(userId);

        return repository.findAllByUsuario(user,pageable).map(mapper::TarefaToTarefaResponseDTO);
    }

    public Tarefa atualizarTarefa(TarefaPutRequestDTO dados) {
        var tarefa = repository.findById(dados.id())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        mapper.updateTarefaFromDTO(dados, tarefa, userRepository);

        return repository.save(tarefa);
    }
}
