package com.autobots.automanager.controles;

import java.util.ArrayList;
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

import com.autobots.automanager.dtos.UsuarioDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.atualizadores.UsuarioAtualizador;
import com.autobots.automanager.modelos.criadores.CriadorUsuario;
import com.autobots.automanager.modelos.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.modelos.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
public class UsuarioControle {
	
	@Autowired
	UsuarioRepositorio repositorio;
	@Autowired
	UsuarioSelecionador selecionador;
	@Autowired
	UsuarioAtualizador atualizador;
	@Autowired
	CriadorUsuario criador;
	@Autowired
	EmpresaRepositorio empresaRepositorio;
	@Autowired
	EmpresaSelecionador empresaSelecionador;
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable Long id) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Usuario> usuarios = repositorio.findAll();
		Usuario selecionado = selecionador.selecionar(usuarios, id);
		if (selecionado == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<Usuario>(selecionado, status);
		}
	}
	
	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> obterUsuarios(){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Usuario> usuarios = repositorio.findAll();
		if (usuarios == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Usuario>>(usuarios, status);
		}
	}
	
	@PostMapping("/usuario/cadastrar")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO){
		HttpStatus status = HttpStatus.CONFLICT;
		List<Empresa> empresas = empresaRepositorio.findAll();
		Empresa empresa = empresaSelecionador.selecionarRazaoSocial(empresas, usuarioDTO.getRazaoSocial());
		Usuario novoUsuario = null;
		if (empresa == null) {
			status = HttpStatus.BAD_REQUEST;
		} else {
			
			if (usuarioDTO.getPerfilUsuario().toString() == "FORNECEDOR") {
				criador.registrarMercadorias(usuarioDTO);
				novoUsuario = criador.criar(usuarioDTO, usuarioDTO.getPerfilUsuario());
			} else {
				novoUsuario = criador.criar(usuarioDTO, usuarioDTO.getPerfilUsuario());
			}
			
			repositorio.save(novoUsuario);
			empresa.getUsuarios().add(novoUsuario);
			empresaRepositorio.save(empresa);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
	
	@PutMapping("/usuario/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario atualizacao){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Usuario> usuarios = repositorio.findAll();
		Usuario usuario = selecionador.selecionar(usuarios, atualizacao.getId());
		if (usuario != null) {
			atualizador.atualizar(usuario, atualizacao);
			repositorio.save(usuario);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/usuario/deletar/{empresaId}/{usuarioId}")
	public ResponseEntity<?> deletarUsuario(@PathVariable Long empresaId, @PathVariable Long usuarioId){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Empresa> empresas = empresaRepositorio.findAll();
		List<Usuario> usuarios = repositorio.findAll();
		List<Usuario> selecionados = new ArrayList<>();
		Empresa empresa = empresaSelecionador.selecionar(empresas, empresaId);
		Usuario selecionado = selecionador.selecionar(usuarios, usuarioId);
		if (empresa != null) {
			for (Usuario usuario : empresa.getUsuarios()) {
				if (usuario.getId().equals(usuarioId)) {
					selecionados.add(usuario);
				}
			}
			empresa.getUsuarios().removeAll(selecionados);
			repositorio.delete(selecionado);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}
