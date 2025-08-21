-- Script para criação das tabelas do sistema To-Do List

CREATE TABLE tarefa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    status VARCHAR(20) NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_conclusao DATETIME
);

CREATE TABLE historico_tarefa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_tarefa BIGINT NOT NULL,
    status_anterior VARCHAR(20) NOT NULL,
    novo_status VARCHAR(20) NOT NULL,
    data_ocorrencia DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_tarefa) REFERENCES tarefa(id)
);
