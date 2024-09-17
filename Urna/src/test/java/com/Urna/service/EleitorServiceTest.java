package com.Urna.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.Urna.entity.Eleitor;
import com.Urna.repository.EleitorRepository;

public class EleitorServiceTest {
	
	@MockBean
    private EleitorRepository eleitorRepository;

    @Autowired
    private EleitorService eleitorService;

    @Test
    public void testSalvarEleitorComDadosIncompletos() {
        Eleitor eleitor = new Eleitor();
        eleitor.setNomeCompleto("João Silva");

        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        Eleitor salvo = eleitorService.salvarEleitor(eleitor);

        assertEquals(Eleitor.Status.PENDENTE, salvo.getStatus());
        verify(eleitorRepository, times(1)).save(eleitor);
    }

    @Test
    public void testSalvarEleitorComDadosCompletos() {
        Eleitor eleitor = new Eleitor();
        eleitor.setNomeCompleto("Maria Souza");
        eleitor.setCpf("123.456.789-00");
        eleitor.setEmail("maria@exemplo.com");

        when(eleitorRepository.save(any(Eleitor.class))).thenReturn(eleitor);

        Eleitor salvo = eleitorService.salvarEleitor(eleitor);

        assertEquals(Eleitor.Status.APTO, salvo.getStatus());
        verify(eleitorRepository, times(1)).save(eleitor);
    }
}
