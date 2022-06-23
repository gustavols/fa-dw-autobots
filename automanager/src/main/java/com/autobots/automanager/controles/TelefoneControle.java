package com.autobots.automanager.controles;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.modelos.TelefoneSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@Controller
public class TelefoneControle {
	
	@Autowired
	private TelefoneRepositorio repositorio;
	@Autowired
	private TelefoneSelecionador selecionador;
	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private ClienteSelecionador clienteSelecionador;
	
	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefones(){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Telefone> telefones = repositorio.findAll();
		if (telefones == null) {
			return new ResponseEntity<List<Telefone>>(status);
		} else {
			adicionadorLink.adicionarLink(telefones);
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Telefone>>(telefones, status);
		}
	}
	
	@GetMapping("/telefone/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Telefone> telefones = repositorio.findAll();
		Telefone selecionado = selecionador.selecionar(telefones, id);
		if (selecionado == null) {
			return new ResponseEntity<Telefone>(status);
		} else {
			adicionadorLink.adicionarLink(selecionado);
			status = HttpStatus.OK;
			return new ResponseEntity<Telefone>(selecionado, status);
		}
	}
	
	@PutMapping("/telefone/cadastro")
	public ResponseEntity<?> cadastrarTelefone(@RequestBody Cliente cliente){
		HttpStatus status = HttpStatus.CONFLICT;
		List<Telefone> telefones = cliente.getTelefones();
		for (Telefone telefone : telefones) {
			if (telefone.getId() != null) {
				return new ResponseEntity<>(status);
			}
		}
		List<Cliente> clientes = clienteRepositorio.findAll();
		Cliente selecionado = clienteSelecionador.selecionar(clientes, cliente.getId());
		if (selecionado == null) {
			status = HttpStatus.BAD_REQUEST;
		} else {
			selecionado.getTelefones().addAll(telefones);
			clienteRepositorio.save(selecionado);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
	
	@PutMapping("/telefone/atualizar")
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao){
		HttpStatus status = HttpStatus.CONFLICT;
		List<Telefone> telefones = repositorio.findAll();
		Telefone selecionado = selecionador.selecionar(telefones, atualizacao.getId());
		if (selecionado != null) {
			TelefoneAtualizador atualizador = new TelefoneAtualizador();
			atualizador.atualizar(selecionado, atualizacao);
			repositorio.save(selecionado);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/telefone/excluir")
	public ResponseEntity<?> excluirTelefone(@RequestBody Cliente exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Cliente> clientes = clienteRepositorio.findAll();
		Cliente clienteSelecionado = clienteSelecionador.selecionar(clientes, exclusao.getId());
		if (clienteSelecionado == null) {
			return new ResponseEntity<>(status);
		} else {
			List<Telefone> telefones = repositorio.findAll();
			List<Telefone> selecionados = new ArrayList<>();
			for (Telefone telefone : exclusao.getTelefones()) {
				Telefone selecionado = selecionador.selecionar(telefones, telefone.getId());
				selecionados.add(selecionado);
			}
			clienteSelecionado.getTelefones().removeAll(selecionados);
			clienteRepositorio.save(clienteSelecionado);
			repositorio.deleteAll(selecionados);
			
			status = HttpStatus.OK;
			return new ResponseEntity<>(status);
		}
	}
}
