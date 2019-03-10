package com.pedrohnf688.api.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedrohnf688.api.entidades.Funcionario;
import com.pedrohnf688.api.repositorio.FuncionarioRepositorio;
import com.pedrohnf688.api.service.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

	private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);
	
	@Autowired
	private FuncionarioRepositorio funcionarioRepositorio;
	
	
	@Override
	public Funcionario persistir(Funcionario funcionario) {
		log.info("Persistindo Funcionario: {}", funcionario);
		return this.funcionarioRepositorio.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		log.info("Buscando Funcionario pelo Cpf: {}",cpf);
		return Optional.ofNullable(this.funcionarioRepositorio.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
		log.info("Buscando Funcionario pelo Email: {}", email);
		return Optional.ofNullable(this.funcionarioRepositorio.findByEmail(email));
	}

	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		log.info("Buscando Funcionario pelo Id:", id);
		return this.funcionarioRepositorio.findById(id);
	}

}
