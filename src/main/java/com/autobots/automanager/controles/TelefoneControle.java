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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	@Autowired
	private TelefoneSelecionador selecionador;
	@Autowired
	private TelefoneRepositorio repositorio;
	@Autowired
	private ClienteSelecionador selecionadorCliente;
	@Autowired
	private ClienteRepositorio repositorioCliente;
	
	@GetMapping("/telefone/{id}")
	public Telefone obterTelefone(@PathVariable long id) {
		List<Telefone> telefones = repositorio.findAll();
		return selecionador.selecionar(telefones, id);
	}
	@GetMapping("/telefones")
	public List<Telefone> obterTelefones(){
		List<Telefone> telefones = repositorio.findAll();
		return telefones;
	}
	@PutMapping("/cadastro")
	public void cadastrarTelefone(@RequestBody Cliente cliente) {
		Cliente alvo = repositorioCliente.getById(cliente.getId());
		alvo.getTelefones().addAll(cliente.getTelefones());
		repositorioCliente.save(alvo);
	}
	@PutMapping("/atualizar")
	public void atualizarTelefone(@RequestBody Telefone atualizacao) {
		Telefone telefone = repositorio.getById(atualizacao.getId());
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefone, atualizacao);
		repositorio.save(telefone);
	}
	@DeleteMapping("/excluir")
	public void excluirTelefone(@RequestBody Cliente cliente) {
		Cliente alvo = repositorioCliente.getById(cliente.getId());
		List<Telefone> telefones = repositorio.findAll();
		List<Telefone> selecionados = new ArrayList<>();
		for (Telefone telefone : cliente.getTelefones()) {
			Telefone selecionado = selecionador.selecionar(telefones, telefone.getId());
			selecionados.add(selecionado);
		}
		alvo.getTelefones().removeAll(selecionados);
		repositorioCliente.save(alvo);
		repositorio.deleteAll(selecionados);
	}
}
