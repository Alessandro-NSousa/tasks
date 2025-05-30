package com.tarefas.controller;

import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.dto.TarefaPutRequestDTO;
import com.tarefas.dto.TarefaRequestDTO;
import com.tarefas.dto.TarefaResponsePutDTO;
import com.tarefas.dto.TarefaResponseDTO;
import com.tarefas.services.TarefaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public ResponseEntity createTask(@RequestBody TarefaRequestDTO data) {

        Tarefa newTarefa = this.tarefaService.createTask(data);

        return ResponseEntity.ok( new TarefaResponseDTO(newTarefa));

    }

    @GetMapping
    public Page<TarefaResponseDTO> getAllTasks(@PageableDefault(sort = {"status"}) Pageable paginacao){

        return this.tarefaService.getAllTasks(paginacao);
    }

    @GetMapping("/{idTask}")
    public ResponseEntity getTask(@PathVariable UUID idTask){

        var task = this.tarefaService.getByTask(idTask);

        return ResponseEntity.ok(task);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizarTarefa(@RequestBody TarefaPutRequestDTO dados){
        var tarefa = this.tarefaService.atualizarTarefa(dados);
        return ResponseEntity.ok(new TarefaResponsePutDTO(tarefa));
    }
}
