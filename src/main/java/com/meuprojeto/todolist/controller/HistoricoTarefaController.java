package com.meuprojeto.todolist.controller;

import com.meuprojeto.todolist.entity.HistoricoTarefa;
import com.meuprojeto.todolist.repository.HistoricoTarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historico")
@CrossOrigin(origins = "*")
public class HistoricoTarefaController {

    @Autowired
    private HistoricoTarefaRepository historicoTarefaRepository;

    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<HistoricoTarefa>> buscarHistoricoPorTarefa(@PathVariable Long tarefaId) {
        List<HistoricoTarefa> historicos = historicoTarefaRepository.findByTarefaIdOrderByDataAlteracaoDesc(tarefaId);
        return ResponseEntity.ok(historicos);
    }

    @GetMapping
    public ResponseEntity<List<HistoricoTarefa>> buscarTodos() {
        List<HistoricoTarefa> historicos = historicoTarefaRepository.findAllByOrderByDataAlteracaoDesc();
        return ResponseEntity.ok(historicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoTarefa> buscarPorId(@PathVariable Long id) {
        return historicoTarefaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}