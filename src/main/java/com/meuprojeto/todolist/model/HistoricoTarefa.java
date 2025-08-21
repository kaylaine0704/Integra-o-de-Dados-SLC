package com.meuprojeto.todolist.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class HistoricoTarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_tarefa", nullable = false)
    private Tarefa tarefa;

    @Enumerated(EnumType.STRING)
    private StatusTarefa statusAnterior;

    @Enumerated(EnumType.STRING)
    private StatusTarefa novoStatus;

    private LocalDateTime dataOcorrencia;

    @PrePersist
    public void prePersist() {
        this.dataOcorrencia = LocalDateTime.now();
    }

    // Getters e Setters
}
