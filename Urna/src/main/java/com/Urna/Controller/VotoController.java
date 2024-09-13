package com.Urna.Controller;

import com.Urna.entity.Voto;
import com.Urna.entity.Apuracao;
import com.Urna.service.VotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votos")
public class VotoController {

    private final VotoService votoService;

    public VotoController(VotoService votoService) {
        this.votoService = votoService;
    }

    @PostMapping("/votar/{eleitorId}")
    public ResponseEntity<String> votar(@PathVariable Long eleitorId, @RequestBody Voto voto) {
        try {
            String hash = votoService.votar(eleitorId, voto);
            return ResponseEntity.ok("Voto registrado com sucesso. Hash de confirmação: " + hash);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao registrar voto: " + e.getMessage());
        }
    }

    @GetMapping("/apuracao")
    public ResponseEntity<Apuracao> realizarApuracao() {
        try {
            Apuracao apuracao = votoService.realizarApuracao();
            return ResponseEntity.ok(apuracao);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
