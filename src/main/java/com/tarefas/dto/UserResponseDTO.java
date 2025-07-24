package com.tarefas.dto;

import com.tarefas.domain.user.UserRole;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String nome,
        String email,
        UserRole role,
        Boolean ativo
) {
}
