package com.Urna.repository;

import com.Urna.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    @Override
    @Query("SELECT COUNT(v) FROM Voto v")
    long count();
    
    long countByCandidatoPrefeitoId(Long candidatoId);
    long countByCandidatoVereadorId(Long candidatoId);
}
