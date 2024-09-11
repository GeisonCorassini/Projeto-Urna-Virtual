package com.Urna.Controller;

import com.Urna.entity.Eleitor;
import com.Urna.service.EleitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/eleitores")
public class EleitorController {

    @Autowired
    private EleitorService eleitorService;

    @PostMapping("/cadastrar")
    public ResponseEntity<Eleitor> cadastrarEleitor(@RequestBody Eleitor eleitor) {
        Eleitor eleitorSalvo = eleitorService.salvarEleitor(eleitor);
        return ResponseEntity.ok(eleitorSalvo);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Eleitor>> listarTodosEleitores() {
        List<Eleitor> eleitores = eleitorService.listarTodos();
        return ResponseEntity.ok(eleitores);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<Eleitor> buscarEleitorPorId(@PathVariable Long id) {
        Optional<Eleitor> eleitor = eleitorService.buscarPorId(id);
        return eleitor.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/atualizar/{id}")
    public ResponseEntity<Eleitor> atualizarEleitor(@PathVariable Long id, @RequestBody Eleitor eleitorAtualizado) {
        Eleitor eleitorAtualizadoSalvo = eleitorService.atualizarEleitor(id, eleitorAtualizado);
        if (eleitorAtualizadoSalvo != null) {
            return ResponseEntity.ok(eleitorAtualizadoSalvo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarEleitor(@PathVariable Long id) {
        eleitorService.deletarEleitor(id);
        return ResponseEntity.noContent().build();
    }
}
