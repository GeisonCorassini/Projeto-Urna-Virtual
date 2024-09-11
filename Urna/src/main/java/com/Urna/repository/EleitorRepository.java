package com.Urna.repository;

import com.Urna.entity.Eleitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EleitorRepository extends JpaRepository<Eleitor, Long> {
}
