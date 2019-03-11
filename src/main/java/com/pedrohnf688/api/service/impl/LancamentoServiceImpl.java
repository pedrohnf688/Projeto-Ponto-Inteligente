package com.pedrohnf688.api.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pedrohnf688.api.entidades.Lancamento;
import com.pedrohnf688.api.repositorio.LancamentoRepositorio;
import com.pedrohnf688.api.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService{

	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);
	
	@Autowired
	private LancamentoRepositorio lancamentoRepositorio;
	 
	@Override
	public Page<Lancamento> buscarFuncionarioPorId(Long funcionarioId, PageRequest pageRequest) {
		log.info("Buscando Lancamento para o Funcionario pelo Id: {}",funcionarioId);
		return this.lancamentoRepositorio.findByFuncionarioId(funcionarioId, pageRequest);
	}

	@Override
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Buscando um Lancamento pelo Id: {}", id);
		return this.lancamentoRepositorio.findById(id);
	}

	@Override
	public Lancamento Persistir(Lancamento lancamento) {
		log.info("Persistindo o Lancamento: {}",lancamento);
		return this.lancamentoRepositorio.save(lancamento);
	}

	@Override
	public void remover(Long id) {
		log.info("Removendo Lancamento pelo Id: {}", id);
		this.lancamentoRepositorio.deleteById(id);;
	}
	

}
