package com.pedrohnf688.api.repositorio;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pedrohnf688.api.entidades.Empresa;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositorioTest {

	@Autowired
	private EmpresaRepositorio empresaRepositorio;
	
	private static final String CNPJ = "434244434265";
	
	@Before
	public void setUp() throws Exception {
		Empresa e = new Empresa();
		e.setRazaoSocial("Empresa de exemplo");
		e.setCnpj(CNPJ);
		this.empresaRepositorio.save(e);
		
	}
	
	public final void tearDown() {
		this.empresaRepositorio.deleteAll();
	} 
	
	@Test
	public void testBuscarPorCnpj() {
		Empresa e = this.empresaRepositorio.findByCnpj(CNPJ);
		
		assertEquals(CNPJ,e.getCnpj());
	}
	
}
