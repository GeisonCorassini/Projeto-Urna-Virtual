package com.Urna.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	    @NotBlank(message = "Nome completo é obrigatório")
	    private String nomeCompleto;

    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank(message = "Número do candidato é obrigatório")
    private String numeroCandidato;

    @Positive(message = "Função deve ser 1 (prefeito) ou 2 (vereador)")
    private Integer funcao; 

    private String status = "ATIVO";

    @Transient
    private Long votosApurados;
}
