package com.tarefas.domain.tarefa;

import com.tarefas.domain.enumeration.Status;
import com.tarefas.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Table(name = "tarefa")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String titulo;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime criacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;
}
