package com.tarefas.services;

import com.tarefas.domain.log.Log;
import com.tarefas.dto.LogRequestDTO;
import com.tarefas.dto.LogResponseDTO;
import com.tarefas.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    @Autowired
    private LogRepository repository;

    public void RegistrarLog(LogRequestDTO dados) {
        repository.save(new Log(dados));

    }
}
