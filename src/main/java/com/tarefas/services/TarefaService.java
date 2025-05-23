package com.tarefas.services;

import com.tarefas.domain.colaborador.Colaborador;
import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.user.User;
import com.tarefas.repository.ColaboradorRepository;
import com.tarefas.repository.TarefaRepository;
import com.tarefas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository repository;
    @Autowired
    private ColaboradorRepository colaboradorRepository;
    @Autowired
    private UserRepository userRepository;

    public Tarefa createTask(Tarefa data) {

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(data.getTitulo());
        tarefa.setDescricao(data.getDescricao());
        tarefa.setCriacao(LocalDateTime.now());

        if (data.getStatus() != null) {
            tarefa.setStatus(data.getStatus());
        } else {
            tarefa.setStatus(Status.PENDENTE);
        }

        User usuario = userRepository.findById(data.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        tarefa.setUsuario(usuario);

        var newTask = repository.save(tarefa);

        return newTask;

    }

    public List<Tarefa> getAllTasks() {

        return repository.findAll();

    }

    public Tarefa getByTask(UUID taskId) {

        Tarefa tarefa = repository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));

        return tarefa;
    }
}
