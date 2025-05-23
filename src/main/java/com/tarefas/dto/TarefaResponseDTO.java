package com.tarefas.dto;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.tarefa.Tarefa;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record TarefaResponseDTO(UUID id, String titulo, String descricao, Status status, LocalDateTime criacao, String colaborador) {

    public TarefaResponseDTO(Tarefa tarefa){
        this(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(),tarefa.getStatus(),tarefa.getCriacao(), tarefa.getUsuario().getNome());
    }
}
