package com.Urna.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private LocalDateTime dataHoraVotacao;

    @NotNull
    private String hash;

    @ManyToOne
    @NotNull(message = "Candidato a prefeito é obrigatório.")
    private Candidato candidatoPrefeito;

    @ManyToOne
    @NotNull(message = "Candidato a vereador é obrigatório.")
    private Candidato candidatoVereador;

    @PrePersist
    public void gerarDataHoraEHash() {
        this.dataHoraVotacao = LocalDateTime.now();
        this.hash = UUID.randomUUID().toString();
    }
}
