package com.pedrohnf688.api.repositorio;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pedrohnf688.api.entidades.Empresa;
import com.pedrohnf688.api.entidades.Funcionario;
import com.pedrohnf688.api.entidades.Lancamento;
import com.pedrohnf688.api.enums.PerfilEnum;
import com.pedrohnf688.api.enums.TipoEnum;
import com.pedrohnf688.api.utils.PasswordUtils;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositorioTest {
	
	@Autowired
	private LancamentoRepositorio lancamentoRepositorio;
	
	@Autowired
	private EmpresaRepositorio empresaRepositorio;
	
	@Autowired
	private FuncionarioRepositorio funcionarioRepositorio;

	private Long funcionarioId;
	
	@Before
	public void setUp() throws Exception {
		Empresa e = this.empresaRepositorio.save(obterDadosEmpresa());
		
		Funcionario f = this.funcionarioRepositorio.save(obterDadosFuncionario(e));
		this.funcionarioId = f.getId();
		
		this.lancamentoRepositorio.save(obterDadosLancamento(f));
		this.lancamentoRepositorio.save(obterDadosLancamento(f));
		
	}
	
	@After
	public final void tearDown() throws Exception {
		this.empresaRepositorio.deleteAll();

	}
	
	@Test
	public void testBuscarLancamentosPorFuncionarioId() {
		List<Lancamento> lancamentos = this.lancamentoRepositorio.findByFuncionarioId(funcionarioId);
		assertEquals(2,lancamentos.size());
		
	}
	
	@Test
	public void testBuscarLancamentosPorFuncionarioIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento> lancamentos = this.lancamentoRepositorio.findByFuncionarioId(funcionarioId, page);
		
		assertEquals(2, lancamentos.getTotalElements());
		
	}
	
	
	private Funcionario obterDadosFuncionario(Empresa empresa) throws NoSuchAlgorithmException{
		int myInt = 34;
		BigDecimal bd = new BigDecimal(myInt);
		
		Funcionario f = new Funcionario();
		f.setNome("Pedro");
		f.setPerfil(PerfilEnum.ROLE_USUARIO);
		f.setSenha(PasswordUtils.gerarBCrypt("123456"));
		f.setCpf("4343242343");
		f.setEmail("email@gmail.com");
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
	
	private Lancamento obterDadosLancamento(Funcionario funcionario) {
		Lancamento l = new Lancamento();
		l.setData(new Date());
		l.setTipo(TipoEnum.INICIO_ALMOCO);
		l.setFuncionario(funcionario);
		return l;
		
	}

	
}
