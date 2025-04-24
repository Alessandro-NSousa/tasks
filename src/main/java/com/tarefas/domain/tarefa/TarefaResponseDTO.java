package com.tarefas.domain.tarefa;

import java.util.UUID;

public record TarefaResponseDTO(UUID id, String titulo, String descricao, String colaborador) {

    public TarefaResponseDTO(Tarefa tarefa){
        this(tarefa.getId(), tarefa.getTitulo(), tarefa.getDescricao(), tarefa.getColaborador().getNome());
    }
}
