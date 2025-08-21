package com.meuprojeto.todolist.service;

import com.meuprojeto.todolist.entity.Tarefa;
import com.meuprojeto.todolist.enums.Status;
import com.meuprojeto.todolist.repository.TarefaRepository;
import com.meuprojeto.todolist.repository.HistoricoTarefaRepository;
import com.meuprojeto.todolist.entity.HistoricoTarefa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private HistoricoTarefaRepository historicoTarefaRepository;

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public Optional<Tarefa> buscarPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    public Tarefa salvar(Tarefa tarefa) {
        tarefa.setDataCriacao(LocalDateTime.now());
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        
        // Registrar no histórico
        registrarHistorico(tarefaSalva, "Tarefa criada");
        
        return tarefaSalva;
    }

    public Tarefa atualizar(Long id, Tarefa tarefaAtualizada) {
        return tarefaRepository.findById(id).map(tarefa -> {
            Status statusAntigo = tarefa.getStatus();
            tarefa.setTitulo(tarefaAtualizada.getTitulo());
            tarefa.setDescricao(tarefaAtualizada.getDescricao());
            tarefa.setStatus(tarefaAtualizada.getStatus());
            tarefa.setDataAtualizacao(LocalDateTime.now());
            
            Tarefa tarefaAtualizadaSalva = tarefaRepository.save(tarefa);
            
            // Registrar mudança de status no histórico
            if (!statusAntigo.equals(tarefaAtualizada.getStatus())) {
                registrarHistorico(tarefaAtualizadaSalva, 
                    String.format("Status alterado de %s para %s", statusAntigo, tarefaAtualizada.getStatus()));
            }
            
            return tarefaAtualizadaSalva;
        }).orElseThrow(() -> new RuntimeException("Tarefa não encontrada com id: " + id));
    }

    public void deletar(Long id) {
        tarefaRepository.findById(id).ifPresent(tarefa -> {
            registrarHistorico(tarefa, "Tarefa deletada");
            tarefaRepository.deleteById(id);
        });
    }

    public List<Tarefa> buscarPorStatus(Status status) {
        return tarefaRepository.findByStatus(status);
    }

    public List<Tarefa> buscarPorTitulo(String titulo) {
        return tarefaRepository.findByTituloContainingIgnoreCase(titulo);
    }

    private void registrarHistorico(Tarefa tarefa, String acao) {
        HistoricoTarefa historico = new HistoricoTarefa();
        historico.setTarefa(tarefa);
        historico.setAcao(acao);
        historico.setDataAlteracao(LocalDateTime.now());
        historicoTarefaRepository.save(historico);
    }
}