package com.tarefas.dto;

import com.tarefas.domain.user.UserRole;

public record RegisterRequestDTO(String email, String password, UserRole role) {
}
