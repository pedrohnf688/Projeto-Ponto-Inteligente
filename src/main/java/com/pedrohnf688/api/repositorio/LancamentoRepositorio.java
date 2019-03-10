package com.pedrohnf688.api.repositorio;

import java.util.List;

import javax.persistence.NamedQuery;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pedrohnf688.api.entidades.Lancamento;

@Transactional(readOnly = true)
@NamedQuery(name = "LancamentoRepositorio.findByFuncionarioId", 
			query = "SELECT lanc FROM Lancamento lanc WHERE lanc.funcionario.id = :funcionarioId")
public interface LancamentoRepositorio extends JpaRepository<Lancamento, Long>{
	
	List<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId);
	
	Page<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId, Pageable pageable);

}
