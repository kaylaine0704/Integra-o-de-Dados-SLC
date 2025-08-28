package com.meuprojeto.todolist.dto;

import com.meuprojeto.todolist.enums.Status;

public class StatusRequest {
    private Status status;

    // Construtor padrão
    public StatusRequest() {
    }

    // Construtor com parâmetro
    public StatusRequest(Status status) {
        this.status = status;
    }

    // Getter e Setter
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
