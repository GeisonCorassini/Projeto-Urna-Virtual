package com.Urna.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Candidato {
	
	public enum StatusCandidato {
	    ATIVO, INATIVO
	}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    private String cpf;

    @NotBlank(message = "Número do candidato é obrigatório")
    @Column(unique = true, nullable = false)
    private String numeroCandidato;

    @NotNull(message = "Função é obrigatória")
    @Positive(message = "Função deve ser 1 (prefeito) ou 2 (vereador)")
    private Integer funcao;

    @Enumerated(EnumType.STRING)
    private StatusCandidato status;

    @Transient
    private Long votosApurados;

    public Candidato(String nomeCompleto, String cpf, String numeroCandidato, Integer funcao) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.numeroCandidato = numeroCandidato;
        this.funcao = funcao;
        this.status = StatusCandidato.ATIVO; // O status padrão é definido pelo serviço
    }
}