package com.algaworks.algafood.api.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.model.CidadeModel;
import com.algaworks.algafood.api.model.input.CidadeInput;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Cidades")
@RestController
@RequestMapping("/cidades")
public class CidadeController {

	@Autowired
	CadastroCidadeService cadastroCidade;
	
	@Autowired
	CidadeRepository cidadeRepository;
	
	@Autowired
	CidadeModelAssembler cidadeModelAssembler;
	
	@Autowired
	CidadeInputDisassembler cidadeInputDisassembler;
	
	@ApiOperation("Lista as cidades")
	@GetMapping
	public List<CidadeModel> listar() {
		return cidadeModelAssembler.toModelList(cidadeRepository.findAll());
	}
	
	@ApiOperation("Busca uma cidade por ID")
	@GetMapping("/{cidadeId}")
	public CidadeModel buscar(
			@ApiParam(value = "ID de uma Cidade", example = "1")
			@PathVariable Long cidadeId) {	
		return cidadeModelAssembler.toModel(cadastroCidade.buscarOuFalhar(cidadeId));
	}
	
	@ApiOperation("Cadastra uma cidade")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel salvar(
			@ApiParam(name = "corpo", value = "Representacao de uma nova cidade")
			@RequestBody @Valid CidadeInput cidadeInput) {	
		try {
			return cidadeModelAssembler.toModel(cadastroCidade.save(cidadeInputDisassembler.toDomainObject(cidadeInput)));
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}	
	}
	
	@ApiOperation("Atualiza uma cidade por ID")
	@PutMapping("/{cidadeId}")
	public CidadeModel atualizar(
			@ApiParam(value = "ID de uma cidade", example = "1")
			@PathVariable Long cidadeId, 
			@ApiParam(name = "corpo", value = "Representacao de uma nova cidade com os novos dados")
			@RequestBody @Valid CidadeInput cidadeInput) {
		
		try {
			Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);			
			
			cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			
			
			return cidadeModelAssembler.toModel(cadastroCidade.save(cidadeAtual));
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
			
	}
	
	@ApiOperation("Exclui uma cidade por ID")
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(
			@ApiParam(value = "ID de uma cidade", example = "1")
			@PathVariable Long cidadeId) {
		cadastroCidade.remover(cidadeId);	
	}
}
