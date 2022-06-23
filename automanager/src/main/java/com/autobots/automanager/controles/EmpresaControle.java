package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.dtos.EmpresaDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelos.atualizadores.EmpresaAtualizador;
import com.autobots.automanager.modelos.criadores.CriadorEmpresa;
import com.autobots.automanager.modelos.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;

@RestController
public class EmpresaControle {
	 
	@Autowired
	EmpresaRepositorio repositorio;
	@Autowired
	EmpresaSelecionador selecionador;
	@Autowired
	CriadorEmpresa criador;
	@Autowired
	EmpresaAtualizador atualizador;
	
	@GetMapping("/empresa/{id}")
	public ResponseEntity<Empresa> obterEmpresa(@PathVariable Long id){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Empresa> empresas = repositorio.findAll();
		Empresa selecionado = selecionador.selecionar(empresas, id);
		if (selecionado == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<Empresa>(selecionado, status);
		}
	}
	
	@GetMapping("/empresas")
	public ResponseEntity<List<Empresa>> obterEmpresas(){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Empresa> empresas = repositorio.findAll();
		if (empresas == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Empresa>>(empresas, status);
		}
	}
	
	@PostMapping("/empresa/criar")
	public ResponseEntity<?> criarEmpresa(@RequestBody EmpresaDTO empresaDTO){
		HttpStatus status = HttpStatus.CONFLICT;
		List<Empresa> empresas = repositorio.findAll();
		Empresa novaEmpresa = criador.criar(empresaDTO);
		Empresa empresa = selecionador.selecionarRazaoSocial(empresas, novaEmpresa.getRazaoSocial());
		if (novaEmpresa.getId() == null && empresa == null) {
			status = HttpStatus.CREATED;
			repositorio.save(novaEmpresa);
		}
		return new ResponseEntity<>(status);
	}
	
	@PutMapping("/empresa/atualizar")
	public ResponseEntity<?> atualizarEmpresa(@RequestBody Empresa atualizacao){
		HttpStatus status = HttpStatus.CONFLICT;
		List<Empresa> empresas = repositorio.findAll();
		Empresa empresa = selecionador.selecionar(empresas, atualizacao.getId());
		if (empresa == null) {
			status = HttpStatus.BAD_REQUEST;
		} else {
			atualizador.atualizar(empresa, atualizacao);
			repositorio.save(empresa);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/empresa/deletar/{id}")
	public ResponseEntity<?> deletarEmpresa(@PathVariable Long id){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Empresa> empresas = repositorio.findAll();
		Empresa selecionada = selecionador.selecionar(empresas, id);
		if (selecionada != null) {
			repositorio.delete(selecionada);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}