package com.Urna.repository;

import com.Urna.entity.Eleitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EleitorRepository extends JpaRepository<Eleitor, Long> {
    List<Eleitor> findByStatusNot(Eleitor.Status status);
}
