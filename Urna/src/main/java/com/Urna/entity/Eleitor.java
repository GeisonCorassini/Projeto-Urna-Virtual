package com.Urna.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    //@Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank(message = "Profissão é obrigatória")
    private String profissao;

    @NotBlank(message = "Telefone celular é obrigatório")
    //@Pattern(regexp = "\\d{11}", message = "O telefone celular deve ter 11 dígitos (DDD + número)")
    private String telefoneCelular;

    //@Pattern(regexp = "\\d{10}", message = "O telefone fixo deve ter 10 dígitos (DDD + número)")
    private String telefoneFixo;

    @Email(message = "E-mail deve ser válido")
    private String email;

    @NotBlank(message = "Status é obrigatório")
    private String status;

}
