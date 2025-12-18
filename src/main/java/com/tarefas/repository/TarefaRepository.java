package com.tarefas.repository;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TarefaRepository extends JpaRepository<Tarefa, UUID> {


    Page<Tarefa> findAllByUsuario(User user, Pageable pageable);
    Page<Tarefa> findByStatusAndAtivoTrue(Status status, Pageable pageable);
    Page<Tarefa> findAllByAtivoTrue(Pageable pageable);
    Page<Tarefa> findByStatus(Status status, Pageable pageable);
    Optional<Tarefa> findByIdAndAtivoTrue(UUID id);


}
