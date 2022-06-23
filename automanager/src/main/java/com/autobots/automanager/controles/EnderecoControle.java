package com.autobots.automanager.controles;

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
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.modelos.EnderecoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@Controller
public class EnderecoControle {
		
		@Autowired
		private EnderecoRepositorio repositorio;
		@Autowired
		private EnderecoSelecionador selecionador;
		@Autowired
		private AdicionadorLinkEndereco adicionadorLink;
		@Autowired
		private ClienteRepositorio clienteRepositorio;
		@Autowired
		private ClienteSelecionador clienteSelecionador;
		
		@GetMapping("/enderecos")
		public ResponseEntity<List<Endereco>> obterEnderecos(){
			HttpStatus status = HttpStatus.NOT_FOUND;
			List<Endereco> enderecos = repositorio.findAll();
			if (enderecos == null) {
				return new ResponseEntity<List<Endereco>>(status);
			}else {
				adicionadorLink.adicionarLink(enderecos);
				status = HttpStatus.FOUND;
				return new ResponseEntity<List<Endereco>>(enderecos, status);
			}
		}
		
		@GetMapping("/endereco/{id}")
		public ResponseEntity<Endereco> obterEndereco(@PathVariable long id){
			HttpStatus status = HttpStatus.NOT_FOUND;
			List<Endereco> enderecos = repositorio.findAll();
			Endereco selecionado = selecionador.selecionar(enderecos, id);
			if (selecionado == null) {
				return new ResponseEntity<Endereco>(status);
			}else {
				adicionadorLink.adicionarLink(selecionado);
				status = HttpStatus.FOUND;
				return new ResponseEntity<Endereco>(selecionado, status);
			}
		}
		@PutMapping("/endereco/cadastro")
		public ResponseEntity<?> cadastrarEndereco(@RequestBody Cliente cliente) {
			HttpStatus status = HttpStatus.CONFLICT;
			List<Cliente> clientes = clienteRepositorio.findAll();
			Cliente clienteSelecionado = clienteSelecionador.selecionar(clientes , cliente.getId());
			if (clienteSelecionado == null) {
				status = HttpStatus.BAD_REQUEST;
			} else if (clienteSelecionado.getEndereco() == null) {
				clienteSelecionado.setEndereco(cliente.getEndereco());
				clienteRepositorio.save(clienteSelecionado);
				status = HttpStatus.OK;
			}
			return new ResponseEntity<>(status);
		}
		
		@PutMapping("/endereco/atualizar")
		public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
			HttpStatus status = HttpStatus.CONFLICT;
			Endereco endereco = repositorio.getById(atualizacao.getId());
			if (endereco != null) {
				EnderecoAtualizador atualizador = new EnderecoAtualizador();
				atualizador.atualizar(endereco, atualizacao);
				repositorio.save(endereco);
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.BAD_REQUEST;
			}
			return new ResponseEntity<>(status);
		}
		
		@DeleteMapping("/endereco/excluir")
		public ResponseEntity<?> excluirEndereco(@RequestBody Endereco exclusao) {
			HttpStatus status = HttpStatus.BAD_REQUEST;
			Endereco endereco = repositorio.getById(exclusao.getId());
			if (endereco != null) {
				List<Cliente> clientes = clienteRepositorio.findAll();
				for (Cliente cliente : clientes) {
					if (cliente.getEndereco().getId().equals(endereco.getId())) {
						cliente.setEndereco(null);
					}
				}
				repositorio.delete(endereco);
				status = HttpStatus.OK;
			}
			return new ResponseEntity<>(status);
		}
}
