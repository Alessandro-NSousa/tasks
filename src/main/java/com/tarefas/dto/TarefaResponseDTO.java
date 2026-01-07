package com.tarefas.dto;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.tarefa.Tarefa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TarefaResponseDTO(
        UUID id,
        String titulo,
        String descricao,
        Status status,
        LocalDate criacao,
        ColaboradorDTO colaborador) {

}
