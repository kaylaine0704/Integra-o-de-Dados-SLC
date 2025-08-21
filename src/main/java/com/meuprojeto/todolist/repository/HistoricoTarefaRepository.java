package com.meuprojeto.todolist.repository;

import com.meuprojeto.todolist.entity.HistoricoTarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoTarefaRepository extends JpaRepository<HistoricoTarefa, Long> {
    
    List<HistoricoTarefa> findByTarefaIdOrderByDataAlteracaoDesc(Long tarefaId);
    
    @Query("SELECT h FROM HistoricoTarefa h WHERE h.tarefa.id = :tarefaId ORDER BY h.dataAlteracao DESC")
    List<HistoricoTarefa> findHistoricoByTarefaId(@Param("tarefaId") Long tarefaId);
    
    List<HistoricoTarefa> findAllByOrderByDataAlteracaoDesc();
}