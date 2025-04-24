package com.tarefas.repository;

import com.tarefas.domain.colaborador.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ColaboradorRepository extends JpaRepository<Colaborador, UUID> {
}
