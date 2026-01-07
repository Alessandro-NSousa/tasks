package com.tarefas.dto;

public record UpdatePasswordDTO(
        String senhaAtual,
        String novaSenha,
        String confirmacaoSenha
) {
}
