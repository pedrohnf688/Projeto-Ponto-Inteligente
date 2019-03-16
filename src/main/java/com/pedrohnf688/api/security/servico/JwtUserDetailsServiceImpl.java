package com.pedrohnf688.api.security.servico;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pedrohnf688.api.entidades.Funcionario;
import com.pedrohnf688.api.security.JwtUserFactory;
import com.pedrohnf688.api.service.FuncionarioService;

@Service("userDetailsService")
public class JwtUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private FuncionarioService funcionarioService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<Funcionario> funcionario = funcionarioService.buscarPorEmail(username);
		
		if(funcionario.isPresent()) {
			return JwtUserFactory.create(funcionario.get());
		}
		
		throw new UsernameNotFoundException("Email não Encontrado.");
	}

}
