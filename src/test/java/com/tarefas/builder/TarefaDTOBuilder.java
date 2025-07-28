package com.tarefas.builder;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.user.User;
import com.tarefas.dto.TarefaRequestDTO;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public class TarefaDTOBuilder {

    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Builder.Default
    private String titulo = "Título padrão";

    @Builder.Default
    private String descricao = "Descrição padrão";

    @Builder.Default
    private Status status = Status.PENDENTE;

    @Builder.Default
    private LocalDateTime criacao = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime ultimaAlteracao = LocalDateTime.now();

    @Builder.Default
    private User usuario = UserDTOBuilder.builder().build().toUser();

    public TarefaRequestDTO buildRequestDTO() {

        return new TarefaRequestDTO(titulo,descricao,status,usuario);
    }

}
