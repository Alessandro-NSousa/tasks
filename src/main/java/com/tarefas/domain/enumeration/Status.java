package com.tarefas.domain.enumeration;

public enum Status {
    PENDENTE("Pendente"),
    ANDAMENTO("Andamento"),
    CONCLUIDO("Concluido");

    private String status;

    Status(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
