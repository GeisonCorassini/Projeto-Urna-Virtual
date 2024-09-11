package com.Urna.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHoraVotacao;
    
    private String Hash;
    
    @ManyToOne
    private Candidato candidatoPrefeito;
    
    @ManyToOne
    private Candidato candidatoVereador;
    
    @PrePersist
    public void gerarDataHoraEHash() {
        this.dataHoraVotacao = LocalDateTime.now();
        this.hash = UUID.randomUUID().toString();
    }
}