package com.tarefas.dto;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.user.User;

import java.util.UUID;

public record TarefaPutRequestDTO(
        String titulo,
        String descricao,
        Status status,
        UUID colaboradorId) {
}
