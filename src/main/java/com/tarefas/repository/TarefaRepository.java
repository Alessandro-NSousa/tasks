package com.tarefas.repository;

import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TarefaRepository extends JpaRepository<Tarefa, UUID> {


    Page<Tarefa> findAllByUsuario(Optional<User> user, Pageable pageable);

    Page<Tarefa> findAllByCriacao(Date criacao, Pageable pageable);

}
