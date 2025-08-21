package com.meuprojeto.todolist.repository;

import com.meuprojeto.todolist.entity.Tarefa;
import com.meuprojeto.todolist.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByStatus(Status status);
    List<Tarefa> findByTituloContainingIgnoreCase(String titulo);
}