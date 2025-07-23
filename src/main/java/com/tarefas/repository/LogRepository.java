package com.tarefas.repository;

import com.tarefas.domain.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogRepository  extends JpaRepository<Log, UUID> {
}
