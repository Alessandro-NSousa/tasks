package com.tarefas.domain.tarefa;

import com.tarefas.domain.enumeration.Status;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record TarefaResponseDTO(UUID id, String titulo, String descricao, Status status, LocalDateTime criacao, String colaborador) {

    public TarefaResponseDTO(Tarefa tarefa){
        this(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(),tarefa.getStatus(),tarefa.getCriacao(), tarefa.getColaborador().getNome());
    }
}
