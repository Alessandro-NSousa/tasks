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

    public static Status fromString(String text) {
        for (Status status : Status.values()) {
            if (status.status.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Nenhum status encontrado: " + text);
    }
}
