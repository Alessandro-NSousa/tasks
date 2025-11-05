package com.tarefas.builder;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.user.User;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponseDTO;
import lombok.Builder;

import java.time.LocalDate;
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
    private LocalDate criacao = LocalDate.now();

    @Builder.Default
    private LocalDateTime ultimaAlteracao = LocalDateTime.now();

    @Builder.Default
    private User usuario = UserDTOBuilder.builder().build().toUser();

    public TarefaRequestDTO buildRequestDTO() {
        return new TarefaRequestDTO(titulo, descricao, status, usuario.getId());
    }

    public TarefaResponseDTO buildResponseDTO() {

        return new TarefaResponseDTO(id, titulo, descricao,status, criacao, usuario.getNome());
    }

    public Tarefa buildEntity() {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(id);
        tarefa.setTitulo(titulo);
        tarefa.setDescricao(descricao);
        tarefa.setStatus(status);
        tarefa.setCriacao(criacao);
        tarefa.setUltimaAlteracao(ultimaAlteracao);
        tarefa.setUsuario(usuario);
        return tarefa;
    }

}
