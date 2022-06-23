package com.autobots.automanager.controles;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelos.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.modelos.selecionadores.ServicoSelecionador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;

@RestController
public class ServicoControle {
	
	@Autowired
	ServicoRepositorio repositorio;
	@Autowired
	ServicoSelecionador selecionador;
	@Autowired
	EmpresaRepositorio empresaRepositorio;
	@Autowired
	EmpresaSelecionador empresaSelecionador;
	
	@GetMapping("/servico/{id}")
	public ResponseEntity<Servico> obterServico(@PathVariable Long id){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Servico> servicos = repositorio.findAll();
		Servico selecionado = selecionador.selecionar(servicos, id);
		if (selecionado == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<Servico>(selecionado, status);
		}
	}
	
	@GetMapping("/servicos")
	public ResponseEntity<List<Servico>> obterServicos(){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Servico> servicos = repositorio.findAll();
		if (servicos == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Servico>>(servicos, status);
		}
	}
	
	@PutMapping("/servico/cadastrar/{empresaId}")
	public ResponseEntity<?> cadastrarServico(@PathVariable Long empresaId, @RequestBody Servico servico){
		HttpStatus status = HttpStatus.CONFLICT;
		List<Empresa> empresas = empresaRepositorio.findAll();
		Empresa empresa = empresaSelecionador.selecionar(empresas, empresaId);
		if (servico.getId() == null) {
			if (empresa == null) {
				status = HttpStatus.BAD_REQUEST;
			} else {
				repositorio.save(servico);
				empresa.getServicos().add(servico);
				empresaRepositorio.save(empresa);
				status = HttpStatus.OK;
			}
		}
		
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/servico/deletar/{empresaId}/{servicoId}")
	public ResponseEntity<?> deletarServico(@PathVariable Long empresaId, @PathVariable Long servicoId){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		List<Servico> selecionados = new ArrayList<>();
		List<Servico> servicos = repositorio.findAll();
		List<Empresa> empresas = empresaRepositorio.findAll();
		
		Servico selecionado = selecionador.selecionar(servicos, servicoId);
		Empresa empresa = empresaSelecionador.selecionar(empresas, empresaId);
		
		if (selecionado != null && empresa != null) {
			for (Servico servico : empresa.getServicos()) {
				if (servico.getId() == servicoId) {
					selecionados.add(servico);
				}
			}
			empresa.getServicos().removeAll(selecionados);
			empresaRepositorio.save(empresa);
			repositorio.delete(selecionado);
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<>(status);
	}
}
