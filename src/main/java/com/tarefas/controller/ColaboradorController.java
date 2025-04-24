package com.tarefas.controller;

import com.tarefas.domain.colaborador.Colaborador;
import com.tarefas.services.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/colaborador")
public class ColaboradorController {
    @Autowired
    private ColaboradorService colaboradorService;

    @PostMapping
    public ResponseEntity<Colaborador> createColaborador(@RequestBody Colaborador colaborador) {

        Colaborador response = colaboradorService.createColaborador(colaborador);

        return ResponseEntity.ok(response);
    }
}
