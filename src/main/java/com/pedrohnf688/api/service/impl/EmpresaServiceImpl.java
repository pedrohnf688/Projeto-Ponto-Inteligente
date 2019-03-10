package com.pedrohnf688.api.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pedrohnf688.api.entidades.Empresa;
import com.pedrohnf688.api.repositorio.EmpresaRepositorio;
import com.pedrohnf688.api.service.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService{

	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
	
	@Autowired
	private EmpresaRepositorio empresaRepositorio;
	
	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		log.info("Buscando uma empresa para o CNPJ: {}", cnpj);
		return Optional.ofNullable(empresaRepositorio.findByCnpj(cnpj));
	}

	@Override
	public Empresa persistir(Empresa empresa) {
		log.info("Perisistindo empresa: {}",empresa);
		return this.empresaRepositorio.save(empresa);
	}

}
