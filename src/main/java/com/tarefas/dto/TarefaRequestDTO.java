package com.tarefas.dto;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TarefaRequestDTO(
        @NotBlank
        String titulo,
        @NotBlank
        String descricao,

        Status status,
        @NotNull
        UUID colaboradorId) {
}
