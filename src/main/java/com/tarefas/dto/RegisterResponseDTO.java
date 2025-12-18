package com.tarefas.dto;

import com.tarefas.domain.user.UserRole;

import java.util.UUID;

public record RegisterResponseDTO(UUID id, String username, UserRole role) {
}
