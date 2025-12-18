package com.tarefas.dto;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.tarefa.Tarefa;

import java.time.LocalDateTime;
import java.util.UUID;

public record TarefaResponsePutDTO(UUID id,
                                   String titulo,
                                   String descricao,
                                   Status status,
                                   String colaborador,
                                   LocalDateTime ultimaAlteracao) {

}
