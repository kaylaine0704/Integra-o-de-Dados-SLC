package com.meuprojetotodolist.controller;

import com.meuprojeto.todolist.dto.StatusRequest;
import com.meuprojeto.todolist.entity.HistoricoTarefa;
import com.meuprojeto.todolist.entity.Tarefa;
import com.meuprojeto.todolist.enums.Status;
import com.meuprojeto.todolist.service.HistoricoTarefaService;
import com.meuprojeto.todolist.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
@CrossOrigin(origins = "*")
@Tag(name = "Tarefas", description = "API para gerenciamento de tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private HistoricoTarefaService historicoTarefaService;

    @Operation(summary = "Listar todas as tarefas", description = "Retorna uma lista com todas as tarefas cadastradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefas listadas com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public List<Tarefa> listarTodas() {
        return tarefaService.listarTodas();
    }

    @Operation(summary = "Buscar tarefa por ID", description = "Retorna uma tarefa específica pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(
            @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long id) {
        return tarefaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar nova tarefa", description = "Cria uma nova tarefa com os dados fornecidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public Tarefa criar(@RequestBody Tarefa tarefa) {
        return tarefaService.salvar(tarefa);
    }

    @Operation(summary = "Atualizar tarefa", description = "Atualiza uma tarefa existente com os dados fornecidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(
            @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long id, 
            @RequestBody Tarefa tarefa) {
        try {
            Tarefa tarefaAtualizada = tarefaService.atualizar(id, tarefa);
            return ResponseEntity.ok(tarefaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deletar tarefa", description = "Remove uma tarefa existente pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar tarefas por status", description = "Retorna uma lista de tarefas filtradas pelo status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefas encontradas com sucesso")
    })
    @GetMapping("/status/{status}")
    public List<Tarefa> buscarPorStatus(
            @Parameter(description = "Status da tarefa", example = "PENDENTE") @PathVariable Status status) {
        return tarefaService.buscarPorStatus(status);
    }

    @Operation(summary = "Buscar tarefas por título", description = "Retorna uma lista de tarefas que contenham o título especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefas encontradas com sucesso")
    })
    @GetMapping("/buscar")
    public List<Tarefa> buscarPorTitulo(
            @Parameter(description = "Título da tarefa para busca", example = "Comprar") @RequestParam String titulo) {
        return tarefaService.buscarPorTitulo(titulo);
    }

    @Operation(summary = "Atualizar status da tarefa", description = "Atualiza apenas o status de uma tarefa específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "400", description = "Status inválido fornecido")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<Tarefa> atualizarStatus(
            @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long id,
            @Parameter(description = "Objeto contendo o novo status da tarefa") @RequestBody StatusRequest statusRequest) {
        try {
            Tarefa tarefaAtualizada = tarefaService.atualizarStatus(id, statusRequest.getStatus());
            return ResponseEntity.ok(tarefaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar histórico da tarefa", description = "Retorna o histórico de alterações de status de uma tarefa específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/{id}/history")
    public ResponseEntity<List<HistoricoTarefa>> buscarHistoricoTarefa(
            @Parameter(description = "ID da tarefa", example = "1") @PathVariable Long id) {
        List<HistoricoTarefa> historico = historicoTarefaService.listarPorTarefa(id);
        return ResponseEntity.ok(historico);
    }
}
