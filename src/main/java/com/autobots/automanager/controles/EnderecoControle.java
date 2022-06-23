package com.autobots.automanager.controles;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.modelo.EnderecoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	@Autowired
	private EnderecoSelecionador selecionador;
	@Autowired
	private EnderecoRepositorio repositorio;
	@Autowired
	private ClienteSelecionador selecionadorCliente;
	@Autowired
	private ClienteRepositorio repositorioCliente;
	
	@GetMapping("/endereco/{id}")
	public Endereco obterEndereco(@PathVariable long id){
		List<Endereco> enderecos = repositorio.findAll();
		return selecionador.selecionar(enderecos, id);
	}
	
	@GetMapping("/enderecos")
	public List<Endereco> obterEnderecos(){
		List<Endereco> enderecos = repositorio.findAll();
		return enderecos;
	}
	
	@PutMapping("/cadastro")
	public void atualizarCliente(@RequestBody Cliente cliente) {
		List<Cliente> clientes = repositorioCliente.findAll();
		Cliente selecionado = selecionadorCliente.selecionar(clientes,  cliente.getId());
		selecionado.setEndereco(cliente.getEndereco());
		repositorioCliente.save(selecionado);
	}
	
	@PutMapping("/atualizar")
	public void atualizarEndereco(@RequestBody Endereco atualizacao) {
		Endereco endereco = repositorio.getById(atualizacao.getId());
		EnderecoAtualizador atualizador = new EnderecoAtualizador();
		atualizador.atualizar(endereco, atualizacao);
		repositorio.save(endereco);
	}
	
	@DeleteMapping("/excluir")
	public void excluirEndereco(@RequestBody Cliente cliente) {
		Cliente alvo = repositorioCliente.getById(cliente.getId());
		List<Endereco> enderecos = repositorio.findAll();
		Endereco selecionado = selecionador.selecionar(enderecos, cliente.getEndereco().getId());
		alvo.setEndereco(null);
		repositorio.delete(selecionado);
	}
}
