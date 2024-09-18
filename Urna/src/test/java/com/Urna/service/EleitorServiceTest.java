package com.Urna.service;

import com.Urna.entity.Eleitor;
import com.Urna.repository.EleitorRepository;
import com.Urna.service.EleitorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EleitorServiceTest {

    @Mock
    private EleitorRepository eleitorRepository;

    @InjectMocks
    private EleitorService eleitorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Teste para salvar eleitor
    @Test
    public void testSalvarEleitorApto() {
        Eleitor eleitor = new Eleitor();
        eleitor.setCpf("123.456.789-00");
        eleitor.setEmail("eleitor@mail.com");

        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        Eleitor result = eleitorService.salvarEleitor(eleitor);

        assertEquals(Eleitor.Status.APTO, result.getStatus());
        verify(eleitorRepository, times(1)).save(eleitor);
    }

    @Test
    public void testSalvarEleitorPendente() {
        Eleitor eleitor = new Eleitor();
        eleitor.setCpf(null);  // CPF não fornecido

        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        Eleitor result = eleitorService.salvarEleitor(eleitor);

        assertEquals(Eleitor.Status.PENDENTE, result.getStatus());
        verify(eleitorRepository, times(1)).save(eleitor);
    }

    // Teste para listar eleitores
    @Test
    public void testListarTodosEleitores() {
        List<Eleitor> eleitores = Arrays.asList(new Eleitor(), new Eleitor());

        when(eleitorRepository.findByStatusNot(Eleitor.Status.INATIVO)).thenReturn(eleitores);

        List<Eleitor> result = eleitorService.listarTodos();

        assertEquals(2, result.size());
        verify(eleitorRepository, times(1)).findByStatusNot(Eleitor.Status.INATIVO);
    }

    // Teste para buscar eleitor por ID
    @Test
    public void testBuscarPorId() {
        Eleitor eleitor = new Eleitor();
        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));

        Optional<Eleitor> result = eleitorService.buscarPorId(1L);

        assertTrue(result.isPresent());
        verify(eleitorRepository, times(1)).findById(1L);
    }

    // Teste para deletar eleitor
    @Test
    public void testDeletarEleitorComSucesso() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.Status.APTO);
        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));

        eleitorService.deletarEleitor(1L);

        verify(eleitorRepository, times(1)).save(eleitor);
        assertEquals(Eleitor.Status.INATIVO, eleitor.getStatus());
    }

    @Test
    public void testDeletarEleitorJaVotou() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.Status.VOTOU);
        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eleitorService.deletarEleitor(1L);
        });

        assertEquals("Usuário já votou. Não foi possível inativá-lo.", exception.getMessage());
        verify(eleitorRepository, never()).save(eleitor);  // Não deve salvar o eleitor
    }

    // Teste para atualizar eleitor
    @Test
    public void testAtualizarEleitor() {
        Eleitor eleitorExistente = new Eleitor();
        eleitorExistente.setStatus(Eleitor.Status.INATIVO);

        Eleitor eleitorAtualizado = new Eleitor();
        eleitorAtualizado.setNomeCompleto("Atualizado");
        eleitorAtualizado.setCpf("111.111.111-11");

        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitorExistente));
        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitorExistente);

        Eleitor result = eleitorService.atualizarEleitor(1L, eleitorAtualizado);

        assertEquals("Atualizado", result.getNomeCompleto());
        assertEquals("111.111.111-11", result.getCpf());
        verify(eleitorRepository, times(1)).save(eleitorExistente);
    }

    // Teste para votar
    @Test
    public void testVotarEleitorApto() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.Status.APTO);

        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));

        eleitorService.votar(1L);

        verify(eleitorRepository, times(1)).save(eleitor);
        assertEquals(Eleitor.Status.VOTOU, eleitor.getStatus());
    }

    @Test
    public void testVotarEleitorPendente() {
        Eleitor eleitor = new Eleitor();
        eleitor.setStatus(Eleitor.Status.PENDENTE);

        when(eleitorRepository.findById(1L)).thenReturn(Optional.of(eleitor));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            eleitorService.votar(1L);
        });

        assertEquals("Usuário com cadastro pendente tentou votar. O usuário será bloqueado!", exception.getMessage());
        verify(eleitorRepository, times(1)).save(eleitor);
        assertEquals(Eleitor.Status.BLOQUEADO, eleitor.getStatus());
    }
}
