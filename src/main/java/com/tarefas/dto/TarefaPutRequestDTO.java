package com.tarefas.dto;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.user.User;

import java.util.UUID;

public record TarefaPutRequestDTO(UUID id, String titulo, String descricao, Status status, User colaborador) {
}
