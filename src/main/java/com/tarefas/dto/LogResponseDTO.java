package com.tarefas.dto;

import com.tarefas.domain.log.Log;

import java.util.UUID;

public record LogResponseDTO(
        UUID id,
        String descricao,
        String classeEntidade,
        UUID identidade) {
    public LogResponseDTO(Log log){
        this(log.getId(), log.getDescricao(), log.getClasseEntidade(), log.getIdentidade());
    }
}
