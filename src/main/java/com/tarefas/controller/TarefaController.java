package com.tarefas.controller;

import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.domain.tarefa.TarefaResponseDTO;
import com.tarefas.services.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;

    @PostMapping
    public ResponseEntity createTask(@RequestBody Tarefa data) {

        Tarefa newTarefa = this.tarefaService.createTask(data);

        return ResponseEntity.ok( new TarefaResponseDTO(newTarefa));

    }

    @GetMapping
    public List<ResponseEntity> getAllTasks(){

        List tasks = this.tarefaService.getAllTasks();

        return tasks;
    }

    @GetMapping("/{idTask}")
    public ResponseEntity getTask(@PathVariable UUID idTask){

        var task = this.tarefaService.getByTask(idTask);

        return ResponseEntity.ok(task);
    }
}
