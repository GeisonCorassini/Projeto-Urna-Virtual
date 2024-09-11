package com.Urna.service;

import com.Urna.entity.Eleitor;
import com.Urna.repository.EleitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EleitorService {

    @Autowired
    private EleitorRepository eleitorRepository;

    public Eleitor salvarEleitor(Eleitor eleitor) {
        return eleitorRepository.save(eleitor);
    }

    public List<Eleitor> listarTodos() {
        return eleitorRepository.findAll();
    }

    public Optional<Eleitor> buscarPorId(Long id) {
        return eleitorRepository.findById(id);
    }

    public void deletarEleitor(Long id) {
        eleitorRepository.deleteById(id);
    }

    public Eleitor atualizarEleitor(Long id, Eleitor eleitorAtualizado) {
        Optional<Eleitor> eleitorExistente = eleitorRepository.findById(id);
        if (eleitorExistente.isPresent()) {
            Eleitor eleitor = eleitorExistente.get();
            eleitor.setNomeCompleto(eleitorAtualizado.getNomeCompleto());
            eleitor.setCpf(eleitorAtualizado.getCpf());
            eleitor.setProfissao(eleitorAtualizado.getProfissao());
            eleitor.setTelefoneCelular(eleitorAtualizado.getTelefoneCelular());
            eleitor.setTelefoneFixo(eleitorAtualizado.getTelefoneFixo());
            eleitor.setEmail(eleitorAtualizado.getEmail());
            eleitor.setStatus(eleitorAtualizado.getStatus());
            return eleitorRepository.save(eleitor);
        } else {
            return null;
        }
    }
}
