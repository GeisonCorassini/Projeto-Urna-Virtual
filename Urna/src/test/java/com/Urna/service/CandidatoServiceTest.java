package com.Urna.service;

import com.Urna.entity.Candidato;
import com.Urna.entity.Candidato.StatusCandidato;
import com.Urna.repository.CandidatoRepository;
import com.Urna.service.CandidatoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CandidatoServiceTest {

    @Mock
    private CandidatoRepository candidatoRepository;

    @InjectMocks
    private CandidatoService candidatoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Teste para salvar um candidato com status padrão ATIVO
    @Test
    public void testSalvarCandidatoComStatusAtivo() {
        Candidato candidato = new Candidato();
        candidato.setNomeCompleto("Candidato Teste");	
        candidato.setFuncao(1); // Prefeito

        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        Candidato candidatoSalvo = candidatoService.salvarCandidato(candidato);

        assertNotNull(candidatoSalvo);
        assertEquals(StatusCandidato.ATIVO, candidatoSalvo.getStatus());
        verify(candidatoRepository, times(1)).save(candidato);
    }

    // Teste para salvar um candidato com status já definido (INATIVO)
    @Test
    public void testSalvarCandidatoComStatusExistente() {
        Candidato candidato = new Candidato();
        candidato.setNomeCompleto("Candidato Teste");
        candidato.setFuncao(1); // Prefeito
        candidato.setStatus(StatusCandidato.INATIVO);

        when(candidatoRepository.save(any(Candidato.class))).thenReturn(candidato);

        Candidato candidatoSalvo = candidatoService.salvarCandidato(candidato);

        assertNotNull(candidatoSalvo);
        assertEquals(StatusCandidato.INATIVO, candidatoSalvo.getStatus());
        verify(candidatoRepository, times(1)).save(candidato);
    }

    // Teste para buscar um candidato por ID com sucesso
    @Test
    public void testBuscarPorIdComSucesso() {
        Candidato candidato = new Candidato();
        candidato.setId(1L);
        candidato.setNomeCompleto("Candidato Teste");

        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));

        Candidato candidatoEncontrado = candidatoService.buscarPorId(1L);

        assertNotNull(candidatoEncontrado);
        assertEquals(1L, candidatoEncontrado.getId());
        assertEquals("Candidato Teste", candidatoEncontrado.getNomeCompleto());
        verify(candidatoRepository, times(1)).findById(1L);
    }

    // Teste para buscar um candidato por ID inexistente
    @Test
    public void testBuscarPorIdInexistente() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.empty());

        Candidato candidatoEncontrado = candidatoService.buscarPorId(1L);

        assertNull(candidatoEncontrado);
        verify(candidatoRepository, times(1)).findById(1L);
    }

    // Teste para deletar um candidato (setar como INATIVO)
    @Test
    public void testDeletarCandidato() {
        Candidato candidato = new Candidato();
        candidato.setId(1L);
        candidato.setStatus(StatusCandidato.ATIVO);

        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidato));

        candidatoService.deletarCandidato(1L);

        assertEquals(StatusCandidato.INATIVO, candidato.getStatus());
        verify(candidatoRepository, times(1)).findById(1L);
        verify(candidatoRepository, times(1)).save(candidato);
    }

    // Teste para deletar candidato inexistente
    @Test
    public void testDeletarCandidatoInexistente() {
        when(candidatoRepository.findById(1L)).thenReturn(Optional.empty());

        candidatoService.deletarCandidato(1L);

        verify(candidatoRepository, times(1)).findById(1L);
        verify(candidatoRepository, never()).save(any(Candidato.class));
    }
}

