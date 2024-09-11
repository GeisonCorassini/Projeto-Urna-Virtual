package com.Urna.service;

import com.Urna.entity.Candidato;
import com.Urna.repository.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    public Candidato salvarCandidato(Candidato candidato) {
        return candidatoRepository.save(candidato);
    }

    public Candidato buscarPorNumeroCandidato(String numeroCandidato) {
        return candidatoRepository.findByNumeroCandidato(numeroCandidato);
    }

    public List<Candidato> listarCandidatosAtivos() {
        return candidatoRepository.findByStatus("ATIVO");
    }

    public Candidato buscarPorId(Long id) {
        return candidatoRepository.findById(id).orElse(null);
    }

    public void deletarCandidato(Long id) {
        Candidato candidato = candidatoRepository.findById(id).orElse(null);
        if (candidato != null) {
            candidato.setStatus("INATIVO");
            candidatoRepository.save(candidato);
        }
    }
    
    public List<Candidato> listarCandidatosInativos() {
        return candidatoRepository.findByStatus("INATIVO");
    }

}
