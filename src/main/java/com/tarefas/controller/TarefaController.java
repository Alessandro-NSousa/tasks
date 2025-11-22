package com.tarefas.controller;

import com.tarefas.domain.tarefa.Tarefa;
import com.tarefas.dto.*;


import com.tarefas.services.TarefaService;
import com.tarefas.services.IAService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;

    private final IAService iaService;

    public TarefaController(IAService iaService) {
        this.iaService = iaService;
    }

    @PostMapping
    public ResponseEntity createTask(@RequestBody TarefaRequestDTO data, UriComponentsBuilder uriBuilder) {

        var newTarefa = this.tarefaService.createTask(data);
        var uri = uriBuilder.path("/api/tarefas/{idTask}").buildAndExpand(newTarefa.id()).toUri();

        return ResponseEntity.created(uri).body(newTarefa);

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

    @GetMapping("user/{user_id}")
    public Page<TarefaResponseDTO> getUser(@PathVariable UUID user_id, @PageableDefault(sort = {"status"}) Pageable paginacao){

        return this.tarefaService.getByUser(user_id, paginacao);
    }

    @GetMapping("status/{status}")
    public Page<TarefaResponseDTO> getTasksByStatus(@PathVariable String status, @PageableDefault(sort = {"status"}) Pageable paginacao){

        return this.tarefaService.getTasksByStatus(status, paginacao);
    }

    @PutMapping("/{id}/update")
    @Transactional
    public ResponseEntity updateTask(@PathVariable UUID id, @RequestBody TarefaPutRequestDTO dados){
        var tarefa = this.tarefaService.atualizarTarefa(id,dados);
        return ResponseEntity.ok(tarefa);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        tarefaService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sugerir")
    public ResponseEntity<SugestaoIAResponseDTO> sugerir(@RequestBody SugestaoIARequestDTO request) {
        String resposta = iaService.gerarResposta(request.texto());
        return ResponseEntity.ok(new SugestaoIAResponseDTO(resposta));
    }

}



