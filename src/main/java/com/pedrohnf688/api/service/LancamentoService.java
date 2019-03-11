package com.pedrohnf688.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.pedrohnf688.api.entidades.Lancamento;

public interface LancamentoService {
	
	
	Page<Lancamento> buscarFuncionarioPorId(Long funcionarioId, PageRequest pageRequest);
	
	Optional<Lancamento> buscarPorId(Long id);
	
	Lancamento Persistir(Lancamento lancamento);
	
	void remover(Long id);

}