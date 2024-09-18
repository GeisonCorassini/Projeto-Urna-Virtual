package com.Urna.service;

import com.Urna.entity.Candidato;
import com.Urna.entity.Voto;
import com.Urna.entity.Eleitor;
import com.Urna.entity.Apuracao;
import com.Urna.repository.CandidatoRepository;
import com.Urna.repository.VotoRepository;
import com.Urna.service.EleitorService;
import com.Urna.service.VotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private EleitorService eleitorService;

    @InjectMocks
    private VotoService votoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Teste para votar com sucesso
    @Test
    public void testVotarComSucesso() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.Status.APTO);

        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);
        candidatoPrefeito.setFuncao(1); // Função de prefeito

        Candidato candidatoVereador = new Candidato();
        candidatoVereador.setId(2L);
        candidatoVereador.setFuncao(2); // Função de vereador

        Voto voto = new Voto();
        voto.setCandidatoPrefeito(candidatoPrefeito);
        voto.setCandidatoVereador(candidatoVereador);

        when(eleitorService.buscarPorId(1L)).thenReturn(Optional.of(eleitor));
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidatoPrefeito));
        when(candidatoRepository.findById(2L)).thenReturn(Optional.of(candidatoVereador));

        String hash = votoService.votar(1L, voto);

        assertNotNull(hash);
        assertEquals(Eleitor.Status.VOTOU, eleitor.getStatus());
        verify(votoRepository, times(1)).save(voto);
        verify(eleitorService, times(1)).salvarEleitor(eleitor);
    }

    // Teste para votar com eleitor pendente
    @Test
    public void testVotarEleitorPendente() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.Status.PENDENTE);

        Voto voto = new Voto();
        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);
        candidatoPrefeito.setFuncao(1);

        voto.setCandidatoPrefeito(candidatoPrefeito);

        when(eleitorService.buscarPorId(1L)).thenReturn(Optional.of(eleitor));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals("Eleitor pendente e foi bloqueado para votar", exception.getMessage());
        verify(eleitorService, times(1)).salvarEleitor(eleitor);
        assertEquals(Eleitor.Status.BLOQUEADO, eleitor.getStatus());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    // Teste para votar com candidato prefeito inválido
    @Test
    public void testVotarComCandidatoPrefeitoInvalido() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.Status.APTO);

        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);
        candidatoPrefeito.setFuncao(2); // Função incorreta

        Voto voto = new Voto();
        voto.setCandidatoPrefeito(candidatoPrefeito);

        when(eleitorService.buscarPorId(1L)).thenReturn(Optional.of(eleitor));
        when(candidatoRepository.findById(1L)).thenReturn(Optional.of(candidatoPrefeito));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals("O candidato escolhido para prefeito é um candidato a vereador. Refaça a requisição!", exception.getMessage());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    // Teste para votar com candidato vereador inválido
    @Test
    public void testVotarComCandidatoVereadorInvalido() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.Status.APTO);

        Candidato candidatoVereador = new Candidato();
        candidatoVereador.setId(2L);
        candidatoVereador.setFuncao(1); // Função incorreta

        Voto voto = new Voto();
        voto.setCandidatoVereador(candidatoVereador);

        when(eleitorService.buscarPorId(1L)).thenReturn(Optional.of(eleitor));
        when(candidatoRepository.findById(2L)).thenReturn(Optional.of(candidatoVereador));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            votoService.votar(1L, voto);
        });

        assertEquals("O candidato escolhido para vereador é um candidato a prefeito. Refaça a requisição!", exception.getMessage());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    // Teste para realizar apuração
    @Test
    public void testRealizarApuracao() {
        Candidato candidatoPrefeito = new Candidato();
        candidatoPrefeito.setId(1L);
        candidatoPrefeito.setFuncao(1);
        candidatoPrefeito.setStatus(Candidato.StatusCandidato.ATIVO);

        Candidato candidatoVereador = new Candidato();
        candidatoVereador.setId(2L);
        candidatoVereador.setFuncao(2);
        candidatoVereador.setStatus(Candidato.StatusCandidato.ATIVO);

        when(candidatoRepository.findAtivosPorFuncao(Candidato.StatusCandidato.ATIVO, 1))
                .thenReturn(Arrays.asList(candidatoPrefeito));
        when(candidatoRepository.findAtivosPorFuncao(Candidato.StatusCandidato.ATIVO, 2))
                .thenReturn(Arrays.asList(candidatoVereador));
        when(votoRepository.countByCandidatoPrefeitoId(1L)).thenReturn(100L);
        when(votoRepository.countByCandidatoVereadorId(2L)).thenReturn(200L);
        when(votoRepository.count()).thenReturn(300L);

        Apuracao apuracao = votoService.realizarApuracao();

        assertEquals(300L, apuracao.getTotalVotos());
        assertEquals(100L, candidatoPrefeito.getVotosApurados());
        assertEquals(200L, candidatoVereador.getVotosApurados());
        verify(votoRepository, times(1)).countByCandidatoPrefeitoId(1L);
        verify(votoRepository, times(1)).countByCandidatoVereadorId(2L);
        verify(votoRepository, times(1)).count();
    }
}
