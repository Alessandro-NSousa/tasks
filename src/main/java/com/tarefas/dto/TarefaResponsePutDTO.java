package com.tarefas.dto;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.tarefa.Tarefa;

import java.util.UUID;

public record TarefaResponsePutDTO(UUID id, String titulo, String descricao, Status status, String colaborador) {
    public TarefaResponsePutDTO(Tarefa tarefa){
        this(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getStatus(),tarefa.getUsuario().getNome());
    }
}
