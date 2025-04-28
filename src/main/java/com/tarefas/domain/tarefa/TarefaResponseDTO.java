package com.tarefas.domain.tarefa;

import com.tarefas.domain.enumeration.Status;

import java.util.UUID;

public record TarefaResponseDTO(UUID id, String titulo, String descricao, Status status, String colaborador) {

    public TarefaResponseDTO(Tarefa tarefa){
        this(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(),tarefa.getStatus(), tarefa.getColaborador().getNome());
    }
}
