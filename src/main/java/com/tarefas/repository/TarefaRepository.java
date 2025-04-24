package com.tarefas.repository;

import com.tarefas.domain.tarefa.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TarefaRepository extends JpaRepository<Tarefa, UUID> {

    Tarefa getTarefaById(UUID id);
}
