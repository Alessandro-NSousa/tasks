package com.tarefas.domain.log;

import com.tarefas.dto.LogRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "log")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private String descricao;
    private String classeEntidade;
    private UUID identidade;

    public Log (LogRequestDTO dados) {
        this.ativo = true;
        this.dataCriacao = LocalDateTime.now();
        this.descricao = dados.descricao();
        this.classeEntidade = dados.classeEntidade();
        this.identidade = dados.identidade();
    }

}
