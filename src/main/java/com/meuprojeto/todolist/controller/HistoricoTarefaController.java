package com.meuprojeto.todolist.controller;

import com.meuprojeto.todolist.entity.HistoricoTarefa;
import com.meuprojeto.todolist.repository.HistoricoTarefaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Histórico de Tarefas", description = "API para gerenciamento do histórico de alterações de tarefas")
@RestController
@RequestMapping("/api/historico")
@CrossOrigin(origins = "*")
public class HistoricoTarefaController {

    @Autowired
    private HistoricoTarefaRepository historicoTarefaRepository;

    @Operation(summary = "Buscar histórico por tarefa", description = "Retorna o histórico de uma tarefa específica pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/tarefa/{tarefaId}")
    public ResponseEntity<List<HistoricoTarefa>> buscarHistoricoPorTarefa(
            @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long tarefaId) {
        List<HistoricoTarefa> historicos = historicoTarefaRepository.findByTarefaIdOrderByDataAlteracaoDesc(tarefaId);
        return ResponseEntity.ok(historicos);
    }

    @Operation(summary = "Buscar todos os históricos", description = "Retorna uma lista de todos os históricos de tarefas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Históricos encontrados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<HistoricoTarefa>> buscarTodos() {
        List<HistoricoTarefa> historicos = historicoTarefaRepository.findAllByOrderByDataAlteracaoDesc();
        return ResponseEntity.ok(historicos);
    }

    @Operation(summary = "Buscar histórico por ID", description = "Retorna um histórico específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Histórico não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoTarefa> buscarPorId(
            @Parameter(description = "ID do histórico", example = "1") @PathVariable Long id) {
        return historicoTarefaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
