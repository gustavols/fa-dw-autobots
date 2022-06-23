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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	@Autowired
	private DocumentoSelecionador selecionador;
	@Autowired
	private DocumentoRepositorio repositorio;
	@Autowired
	private ClienteSelecionador selecionadorCliente;
	@Autowired
	private ClienteRepositorio repositorioCliente;
	
	@GetMapping("/documento/{id}")
	public Documento obterDocumento(@PathVariable long id) {
		List<Documento> documentos = repositorio.findAll();
		return selecionador.selecionar(documentos, id);
	}
	@GetMapping("/documentos")
	public List<Documento> obterDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		return documentos;
	}
	@PutMapping("/cadastro")
	public void cadastrarDocumentos(@RequestBody Cliente cliente) {
		Cliente alvo = repositorioCliente.getById(cliente.getId());
		alvo.getDocumentos().addAll(cliente.getDocumentos());
		repositorioCliente.save(alvo);
	}
	@PutMapping("/atualizar")
	public void atualizarDocumento(@RequestBody Documento atualizacao) {
		Documento documento = repositorio.getById(atualizacao.getId());
		DocumentoAtualizador atualizador = new DocumentoAtualizador();
		atualizador.atualizar(documento, atualizacao);
		repositorio.save(documento);
	}
	@DeleteMapping("/excluir")
	public void excluirDocumento(@RequestBody Cliente exclusao) {
		Cliente alvo = repositorioCliente.getById(exclusao.getId());
		List<Documento> documentos = repositorio.findAll();
		List<Documento> selecionados = new ArrayList<>();
		for (Documento documento : exclusao.getDocumentos()) {
			Documento selecionado = selecionador.selecionar(documentos, documento.getId());
			selecionados.add(selecionado);
		}
		alvo.getDocumentos().removeAll(selecionados);
		repositorioCliente.save(alvo);
		repositorio.deleteAll(selecionados);
	}
}
