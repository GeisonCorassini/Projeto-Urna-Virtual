package com.Urna.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Apuracao {

    private long totalVotos;
    private List<Candidato> candidatosPrefeito;
    private List<Candidato> candidatosVereador;

    public Apuracao() {
    }

    public Apuracao(long totalVotos, List<Candidato> candidatosPrefeito, List<Candidato> candidatosVereador) {
        this.totalVotos = totalVotos;
        this.candidatosPrefeito = candidatosPrefeito;
        this.candidatosVereador = candidatosVereador;
    }

    public void adicionarVoto() {
        this.totalVotos++;
    }
}

