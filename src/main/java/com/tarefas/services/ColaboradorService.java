package com.tarefas.services;

import com.tarefas.domain.colaborador.Colaborador;
import com.tarefas.repository.ColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    public Colaborador createColaborador(Colaborador data){
        Colaborador newColaborador = new Colaborador();

        newColaborador.setNome(data.getNome());

        var body = colaboradorRepository.save(newColaborador);
        return body;
    }
}
