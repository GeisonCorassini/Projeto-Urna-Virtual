package com.Urna.Controller;

import com.Urna.entity.Candidato;
import com.Urna.service.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
public class CandidatoController {

    @Autowired
    private CandidatoService candidatoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Candidato> criarCandidato(@RequestBody Candidato candidato) {
        Candidato salvo = candidatoService.salvarCandidato(candidato);
        return new ResponseEntity<>(salvo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidato> buscarCandidatoPorId(@PathVariable Long id) {
        Candidato candidato = candidatoService.buscarPorId(id);
        if (candidato != null && "ATIVO".equals(candidato.getStatus())) {
            return new ResponseEntity<>(candidato, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCandidato(@PathVariable Long id) {
        candidatoService.deletarCandidato(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
