package com.tarefas.services;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.user.User;
import com.tarefas.dto.TarefaPutRequestDTO;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
import com.tarefas.repository.TarefaRepository;
import com.tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository repository;
    @Autowired
    private UserRepository userRepository;

    public Tarefa createTask(TarefaRequestDTO data) {

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(data.titulo());
        tarefa.setDescricao(data.descricao());
        tarefa.setCriacao(LocalDateTime.now());

        if (data.status() != null) {
            tarefa.setStatus(data.status());
        } else {
            tarefa.setStatus(Status.PENDENTE);
        }

        User usuario = userRepository.findById(data.colaborador().getId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        tarefa.setUsuario(usuario);

        var newTask = repository.save(tarefa);

        return newTask;

    }

    public Page<TarefaResponseDTO> getAllTasks(Pageable pageable) {

        return repository.findAll(pageable).map(TarefaResponseDTO::new);

    }

    public Tarefa getByTask(UUID taskId) {

        Tarefa tarefa = repository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));

        return tarefa;
    }

    public Page<TarefaResponseDTO> getByUser(UUID userId, Pageable pageable) {

        var user = userRepository.findById(userId);
        var tasks = repository.findAllByUsuario(user, pageable).map(TarefaResponseDTO::new);

        return tasks;
    }

    public Tarefa atualizarTarefa(TarefaPutRequestDTO dados) {
        var tarefa = repository.getReferenceById(dados.id());

        if (dados.titulo() !=null)
            tarefa.setTitulo(dados.titulo());

        if (dados.descricao() !=null)
            tarefa.setDescricao(dados.descricao());

        if (dados.status()!=null)
            tarefa.setStatus(dados.status());

        if (dados.colaborador()!=null) {
            User user = userRepository.getReferenceById(dados.colaborador().getId());
            tarefa.setUsuario(user);
        }

        return tarefa;
    }
}
