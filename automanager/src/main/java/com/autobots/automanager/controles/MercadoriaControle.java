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

import com.autobots.automanager.dtos.MercadoriaDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.criadores.CriadorMercadoria;
import com.autobots.automanager.modelos.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.modelos.selecionadores.MercadoriaSelecionador;
import com.autobots.automanager.modelos.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
public class MercadoriaControle {
	
	@Autowired
	MercadoriaRepositorio repositorio;
	@Autowired
	MercadoriaSelecionador selecionador;
	@Autowired
	CriadorMercadoria criador;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@Autowired
	UsuarioSelecionador usuarioSelecionador;
	@Autowired
	EmpresaRepositorio empresaRepositorio;
	@Autowired
	EmpresaSelecionador empresaSelecionador;
	
	@GetMapping("/mercadoria/{id}")
	public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable Long id){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Mercadoria> mercadorias = repositorio.findAll();
		Mercadoria mercadoria = selecionador.selecionar(mercadorias, id);
		if (mercadoria == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<Mercadoria>(mercadoria, status);
		}
	}
	
	@GetMapping("/mercadorias")
	public ResponseEntity<List<Mercadoria>> obterMercadorias(){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Mercadoria> mercadorias = repositorio.findAll();
		if (mercadorias == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Mercadoria>>(mercadorias, status);
		}
	}
	
	@PutMapping("/mercadoria/cadastro")
	public ResponseEntity<?> cadastrarMercadoria(@RequestBody MercadoriaDTO mercadoriaDTO){
		HttpStatus status = HttpStatus.CONFLICT;
		Mercadoria mercadoria = null;
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		List<Empresa> empresas = empresaRepositorio.findAll();
		Usuario fornecedor = usuarioSelecionador.selecionar(usuarios, mercadoriaDTO.getFornecedorId());
		
		if (mercadoriaDTO.getMercadoria().getId() == null) {
			mercadoria = criador.criar(mercadoriaDTO);
		} else {
			return new ResponseEntity<>(status);
		}
		
		if (fornecedor == null) {
			status = HttpStatus.BAD_REQUEST;
		} else {
			status = HttpStatus.OK;
			fornecedor.getMercadorias().add(mercadoria);
			
			if (mercadoriaDTO.getRazaoSocial() != null) {
				Empresa empresa = empresaSelecionador.selecionarRazaoSocial(empresas, mercadoriaDTO.getRazaoSocial());
				
				if (empresa != null) {
					empresa.getMercadorias().add(mercadoria);
				}
			}
			repositorio.save(mercadoria);
		}
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/mercadoria/deletar/{empresaId}/{usuarioId}/{mercadoriaId}")
	public ResponseEntity<?> excluirMercadoria(@PathVariable Long empresaId, @PathVariable Long usuarioId, @PathVariable Long mercadoriaId){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		List<Empresa> empresas = empresaRepositorio.findAll();
		List<Mercadoria> mercadorias = repositorio.findAll();
		
		Mercadoria mercadoria = selecionador.selecionar(mercadorias, mercadoriaId);
		Usuario fornecedor = usuarioSelecionador.selecionar(usuarios, usuarioId);
		Empresa empresa = empresaSelecionador.selecionar(empresas, empresaId);
		
		if (mercadoria != null) {
			List<Mercadoria> selecionados = new ArrayList<>();
			for(Mercadoria exclusao : fornecedor.getMercadorias()) {
				if (exclusao.getId().equals(mercadoria.getId())) {
					selecionados.add(exclusao);
				}
			}
			
			fornecedor.getMercadorias().removeAll(selecionados);
			empresa.getMercadorias().removeAll(selecionados);
			usuarioRepositorio.save(fornecedor);
			empresaRepositorio.save(empresa);
			repositorio.delete(mercadoria);
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<>(status);
	}
}
