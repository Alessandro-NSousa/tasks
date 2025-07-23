package com.tarefas.dto;

import com.tarefas.domain.log.Log;

import java.time.LocalDateTime;
import java.util.UUID;

public record LogRequestDTO(
        String descricao,
        String classeEntidade,
        UUID identidade) {
}
