package com.pedrohnf688.api.service;

import java.util.Optional;

import com.pedrohnf688.api.entidades.Empresa;

public interface EmpresaService {
	
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	Empresa persistir(Empresa empresa);

}
