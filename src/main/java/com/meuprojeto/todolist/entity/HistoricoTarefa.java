package com.meuprojeto.todolist.entity;

import com.meuprojeto.todolist.enums.Status;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_tarefa")
public class HistoricoTarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @Column(name = "tarefa_id", insertable = false, updatable = false)
    private Long tarefaId;

    @Enumerated(EnumType.STRING)
    private Status statusAnterior;

    @Enumerated(EnumType.STRING)
    private Status novoStatus;

    private String acao;

    @Column(name = "data_alteracao")
    private LocalDateTime dataAlteracao;

    @PrePersist
    public void prePersist() {
        if (this.dataAlteracao == null) {
            this.dataAlteracao = LocalDateTime.now();
        }
    }

    // Construtores
    public HistoricoTarefa() {}

    public HistoricoTarefa(Tarefa tarefa, String acao) {
        this.tarefa = tarefa;
        this.acao = acao;
        this.dataAlteracao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
        if (tarefa != null) {
            this.tarefaId = tarefa.getId();
        }
    }

    public Long getTarefaId() {
        return tarefaId;
    }

    public void setTarefaId(Long tarefaId) {
        this.tarefaId = tarefaId;
    }

    public Status getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(Status statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public Status getNovoStatus() {
        return novoStatus;
    }

    public void setNovoStatus(Status novoStatus) {
        this.novoStatus = novoStatus;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}