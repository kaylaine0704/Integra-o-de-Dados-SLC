package com.meuprojeto.todolist.service;

import com.meuprojeto.todolist.model.HistoricoTarefa;
import com.meuprojeto.todolist.repository.HistoricoTarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoTarefaService {
    @Autowired
    private HistoricoTarefaRepository historicoTarefaRepository;

    public List<HistoricoTarefa> listarPorTarefa(Long idTarefa) {
        return historicoTarefaRepository.findByTarefaIdOrderByDataOcorrenciaDesc(idTarefa);
    }
}
