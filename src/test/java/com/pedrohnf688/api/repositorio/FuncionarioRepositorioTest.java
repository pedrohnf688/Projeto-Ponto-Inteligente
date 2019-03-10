package com.pedrohnf688.api.repositorio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pedrohnf688.api.entidades.Empresa;
import com.pedrohnf688.api.entidades.Funcionario;
import com.pedrohnf688.api.enums.PerfilEnum;
import com.pedrohnf688.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositorioTest {
	
	@Autowired
	private FuncionarioRepositorio funcionarioRepositorio;
	
	@Autowired
	private EmpresaRepositorio empresaRepositorio;

	private static final String EMAIL = "email@gmail.com";
	private static final String CPF = "346464747";
	
	@Before
	public void setUp() throws Exception{
		Empresa e = this.empresaRepositorio.save(obterDadosEmpresa());
		this.funcionarioRepositorio.save(obterDadosFuncionario(e));
		
	}
	
	@After
	public final void tearDown() {
		this.empresaRepositorio.deleteAll();
	}
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		Funcionario f = this.funcionarioRepositorio.findByEmail(EMAIL);
		assertEquals(EMAIL, f.getEmail());
	}
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		Funcionario f = this.funcionarioRepositorio.findByCpf(CPF);
		assertEquals(CPF, f.getCpf());	
	}
	
	@Test
	public void testBuscarFuncionarioPorEmailECpf() {
		Funcionario f = this.funcionarioRepositorio.findByCpfOrEmail(CPF, EMAIL);
		assertNotNull(f);
	}
	
	@Test
	public void testBucarFuncionarioPorEmailOuCpfParaEmailInvalido() {
		Funcionario f = this.funcionarioRepositorio.findByCpfOrEmail(CPF, "email@invalido.com");
		assertNotNull(f);
	}
	
	@Test
	public void testBucarFuncionarioPorEmailOuCpfParaCpfInvalido() {
		Funcionario f = this.funcionarioRepositorio.findByCpfOrEmail("123450089", EMAIL);
		assertNotNull(f);
	}
	
	
	
	private Funcionario obterDadosFuncionario(Empresa empresa) throws NoSuchAlgorithmException{
		int myInt = 34;
		BigDecimal bd = new BigDecimal(myInt);
		
		Funcionario f = new Funcionario();
		f.setNome("Pedro");
		f.setPerfil(PerfilEnum.ROLE_USUARIO);
		f.setSenha(PasswordUtils.gerarBCrypt("123456"));
		f.setCpf(CPF);
		f.setEmail(EMAIL);
		f.setEmpresa(empresa);
		f.setQtdHorasAlmoco((float) 20);
		f.setQtdHorasTrabalhoDia((float) 10);
		f.setValorHora(bd);
		return f;
	
		
	}
	
	private Empresa obterDadosEmpresa() {
		Empresa e = new Empresa();
		e.setRazaoSocial("Empresa exemplo");
		e.setCnpj("43423423422");
		return e;

	}
	
	
}
