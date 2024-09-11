package com.Urna.repository;

import com.Urna.entity.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {
    Candidato findByNumeroCandidato(String numeroCandidato);
    
    List<Candidato> findByStatus(String status);
}
