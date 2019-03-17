package com.pedrohnf688.api.controllers;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.text.ParseException;
import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pedrohnf688.api.dtos.LancamentoDto;
import com.pedrohnf688.api.entidades.Funcionario;
import com.pedrohnf688.api.entidades.Lancamento;
import com.pedrohnf688.api.enums.TipoEnum;
import com.pedrohnf688.api.response.Response;
import com.pedrohnf688.api.service.FuncionarioService;
import com.pedrohnf688.api.service.LancamentoService;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
	private final SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;

	public LancamentoController() {
	}

	
	@GetMapping(value = "/funcionario/{funcionarioId}")
	public ResponseEntity<Response<Page<LancamentoDto>>> ListarPorFuncionarioId(@PathVariable("funcionarioId") Long funcionarioId, 
			@RequestParam(value = "pag", defaultValue = "0") int pag, @RequestParam(value = "ord", defaultValue = "id") String ord, 
			@RequestParam(value = "dir", defaultValue = "DESC") String dir){
	
		log.info("Buscando Lancamento por ID do Funcionario: {}, pagina: {}", funcionarioId, pag);
		
		Response<Page<LancamentoDto>> response = new Response<Page<LancamentoDto>>();
		
		PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		
		Page<Lancamento> lancamentos = this.lancamentoService.buscarFuncionarioPorId(funcionarioId, pageRequest);
		Page<LancamentoDto> lancamentosDto = lancamentos.map(lancamento -> this.converterLancamentoDto(lancamento));
		
		response.setData(lancamentosDto);
		
		return ResponseEntity.ok(response);
	}
	

	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id){
		log.info("Listando Lancamento por Id: {}",id);
		
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);
		
		if(!lancamento.isPresent()) {
			log.info("Lancamento não encontrado por Id: {}", id);
			
			response.getErrors().add("Lancamento não para o id: " + id);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.converterLancamentoDto(lancamento.get()));
	
		return ResponseEntity.ok(response);
	}
	
	@PostMapping
	public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
			BindingResult result) throws ParseException {

		log.info("Adicionando Lancamento: {}",lancamentoDto.toString());
		
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		
		validarFuncionario(lancamentoDto, result);
		Lancamento lancamento = this.converterParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando lancamento:{}",result.getAllErrors());
			
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
		
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = this.lancamentoService.Persistir(lancamento);
		response.setData(this.converterLancamentoDto(lancamento));		
		
		return ResponseEntity.ok(response);
		
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
		
		log.info("Atualizando o Lancamento:{}",lancamentoDto.toString());
	
		Response<LancamentoDto> response = new Response<LancamentoDto>();
		
		validarFuncionario(lancamentoDto, result);
		
		lancamentoDto.setId(Optional.of(id));
		
		Lancamento lancamento = this.converterParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando lancamento:{}", result.getAllErrors());
			
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = this.lancamentoService.Persistir(lancamento);
		response.setData(this.converterLancamentoDto(lancamento));
		
		return ResponseEntity.ok(response);
	}
	
	
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id){
		
		log.info("Removendo lancamento: {}", id);
		
		Response<String> response = new Response<String>();
		
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);
		
		if(!lancamento.isPresent()) {
			log.info("Erro ao remover devido ao lancamento Id: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover lancamento. Resgistro não para o Id:" + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		this.lancamentoService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	
	
	
	private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
		
		if(lancamentoDto.getFuncionarioId() == null) {
			result.addError(new ObjectError("funcionario","Funcionario não informado."));
			return;
		}
		
		log.info("Validando o Funcionario:{}", lancamentoDto.getFuncionarioId());
		
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
		
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario","Funcionario não encontrado. ID inexistente."));
			
		}
	}
	
	
	private LancamentoDto converterLancamentoDto(Lancamento lancamento) {
		LancamentoDto lancamentoDto = new LancamentoDto();
	
		lancamentoDto.setId(Optional.of(lancamento.getId()));
		lancamentoDto.setData(this.dateFormate.format(lancamento.getData()));
		lancamentoDto.setTipo(lancamento.getTipo().toString());
		lancamentoDto.setDescricao(lancamento.getDescricao());
		lancamentoDto.setLocalizacao(lancamento.getLocalizacao());
		lancamentoDto.setFuncionarioId(lancamento.getFuncionario().getId());
		
		return lancamentoDto;
	}
	
	
	private Lancamento converterParaLancamento(LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
		
		Lancamento lancamento = new Lancamento();
		
		if(lancamentoDto.getId().isPresent()) {
			Optional<Lancamento> lanc = this.lancamentoService.buscarPorId(lancamentoDto.getId().get());
			if(lanc.isPresent()) {
				lancamento = lanc.get();
			}else {
				result.addError(new ObjectError("lancamento", "Lancamento não encontrado."));
			}
		}else {
			lancamento.setFuncionario(new Funcionario());
			lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
		}
		
		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
		lancamento.setData(this.dateFormate.parse(lancamentoDto.getData()));
		
		
		if(EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
			lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
		}else {
			result.addError(new ObjectError("Tipo", "Tipo inválido"));
		}
		
		
		return lancamento;
		
	}
	
	
}
