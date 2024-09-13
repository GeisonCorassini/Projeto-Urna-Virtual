package com.Urna.entity;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Eleitor {

    public enum Status {
        APTO, INATIVO, BLOQUEADO, PENDENTE, VOTOU
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    //@CPF(message = "CPF invalido")  
    private String cpf;

    @NotBlank(message = "Profissão é obrigatória")
    private String profissao;

    //@Pattern(regexp = "\\(\\d{2}\\) \\d{5}-\\d{4}", message = "Telefone celular deve estar no formato (XX) XXXXX-XXXX")
    @NotBlank(message = "Telefone celular é obrigatório")
    private String telefoneCelular;

    //@Pattern(regexp = "\\(\\d{2}\\) \\d{4}-\\d{4}", message = "Telefone fixo deve estar no formato (XX) XXXX-XXXX")
    private String telefoneFixo;

    @Email(message = "E-mail deve ser válido")
    private String email;

    @Enumerated(EnumType.STRING)
    private Status status;
}
