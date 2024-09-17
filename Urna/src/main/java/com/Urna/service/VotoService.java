	package com.Urna.service;
	
	import com.Urna.entity.Candidato;
	import com.Urna.entity.Candidato.StatusCandidato;
	import com.Urna.entity.Voto;
	import com.Urna.entity.Apuracao;
	import com.Urna.repository.CandidatoRepository;
	import com.Urna.repository.VotoRepository;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;
	import com.Urna.entity.Eleitor;
	
	import java.time.LocalDateTime;
	import java.util.List;
	import java.util.UUID;
	
	@Service
	public class VotoService {
	
	    @Autowired
	    private VotoRepository votoRepository;
	
	    @Autowired
	    private CandidatoRepository candidatoRepository;
	
	    @Autowired
	    private EleitorService eleitorService;
	
	    public String votar(Long eleitorId, Voto voto) {
	        Eleitor eleitor = eleitorService.buscarPorId(eleitorId)
	                .orElseThrow(() -> new RuntimeException("Eleitor não encontrado"));
	
	        if (Eleitor.Status.PENDENTE.equals(eleitor.getStatus())) {
	            eleitor.setStatus(Eleitor.Status.BLOQUEADO);
	            eleitorService.salvarEleitor(eleitor);
	            throw new RuntimeException("Eleitor pendente e foi bloqueado para votar");
	        }
	
	        if (!Eleitor.Status.APTO.equals(eleitor.getStatus())) {
	            throw new RuntimeException("Eleitor inapto para votação");
	        }
	
	        if (!isCandidatoPrefeito(voto.getCandidatoPrefeito())) {
	            throw new RuntimeException("O candidato escolhido para prefeito é um candidato a vereador. Refaça a requisição!");
	        }
	
	        if (!isCandidatoVereador(voto.getCandidatoVereador())) {
	            throw new RuntimeException("O candidato escolhido para vereador é um candidato a prefeito. Refaça a requisição!");
	        }
	
	        voto.setDataHoraVotacao(LocalDateTime.now());
	        voto.setHash(UUID.randomUUID().toString());
	
	        votoRepository.save(voto);
	
	        eleitor.setStatus(Eleitor.Status.VOTOU);
	        eleitorService.salvarEleitor(eleitor);
	
	        return voto.getHash();
	    }
	
	
	
	    private boolean isCandidatoPrefeito(Candidato candidato) {
	        return candidatoRepository.findById(candidato.getId())
	                .map(c -> c.getFuncao() == 1)
	                .orElse(false);
	    }
	
	    private boolean isCandidatoVereador(Candidato candidato) {
	        return candidatoRepository.findById(candidato.getId())
	                .map(c -> c.getFuncao() == 2)
	                .orElse(false);
	    }
	
	    public Apuracao realizarApuracao() {
	    	List<Candidato> candidatosPrefeito = candidatoRepository.findAtivosPorFuncao(StatusCandidato.ATIVO, 1);  // 1 = prefeito
	    	List<Candidato> candidatosVereador = candidatoRepository.findAtivosPorFuncao(StatusCandidato.ATIVO, 2);  // 2 = vereador
	
	        for (Candidato candidato : candidatosPrefeito) {
	            long votosPrefeito = votoRepository.countByCandidatoPrefeitoId(candidato.getId());
	            candidato.setVotosApurados(votosPrefeito);
	        }
	
	        for (Candidato candidato : candidatosVereador) {
	            long votosVereador = votoRepository.countByCandidatoVereadorId(candidato.getId());
	            candidato.setVotosApurados(votosVereador);
	        }
	
	        candidatosPrefeito.sort((c1, c2) -> Long.compare(c2.getVotosApurados(), c1.getVotosApurados()));
	        candidatosVereador.sort((c1, c2) -> Long.compare(c2.getVotosApurados(), c1.getVotosApurados()));
	
	        Apuracao apuracao = new Apuracao();
	        apuracao.setTotalVotos(votoRepository.count());
	        apuracao.setCandidatosPrefeito(candidatosPrefeito);
	        apuracao.setCandidatosVereador(candidatosVereador);
	
	        return apuracao;
	    }
	}
