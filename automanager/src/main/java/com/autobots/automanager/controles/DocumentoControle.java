package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.List;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.modelos.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@Controller
public class DocumentoControle {
	
	@Autowired
	private DocumentoRepositorio repositorio;
	@Autowired
	private DocumentoSelecionador selecionador;
	@Autowired
	private AdicionadorLinkDocumento adicionadorLink;
	@Autowired
	private ClienteSelecionador clienteSelecionador;
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@GetMapping("/documentos")
	public ResponseEntity<List<Documento>> obterDocumetos(){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Documento> documentos = repositorio.findAll();
		if (documentos == null) {
			return new ResponseEntity<List<Documento>>(status);
		} else {
			adicionadorLink.adicionarLink(documentos);
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Documento>>(documentos, status);
		}
	}
	
	@GetMapping("/documento/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Documento> documentos = repositorio.findAll();
		Documento selecionado = selecionador.selecionar(documentos, id);
		if (selecionado == null) {
			return new ResponseEntity<>(status);
		} else {
			adicionadorLink.adicionarLink(selecionado);
			status = HttpStatus.OK;
			return new ResponseEntity<Documento>(selecionado, status);
		}
	}
	
	@PutMapping("/documento/cadastro")
	public ResponseEntity<?> cadastrarDocumento(@RequestBody Cliente cliente){
		HttpStatus status = HttpStatus.CONFLICT;
		List<Documento> documentos = cliente.getDocumentos();
		for (Documento documento : documentos) {
			if (documento.getId() != null) {
				return new ResponseEntity<>(status);
			} 
		}
		List<Cliente> clientes = clienteRepositorio.findAll();
		Cliente selecionado = clienteSelecionador.selecionar(clientes, cliente.getId());
		if (selecionado == null) {
			status = HttpStatus.BAD_REQUEST;
		} else {
			selecionado.getDocumentos().addAll(documentos);
			clienteRepositorio.save(selecionado);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
	
	@PutMapping("/documento/atualizar")
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao){
		HttpStatus status = HttpStatus.CONFLICT;
		Documento documento = repositorio.getById(atualizacao.getId());
		if (documento != null) {
			DocumentoAtualizador atualizador = new DocumentoAtualizador();
			atualizador.atualizar(documento, atualizacao);
			repositorio.save(documento);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/documento/excluir")
	public ResponseEntity<?> excluirDocumento(@RequestBody Cliente exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<Cliente> clientes = clienteRepositorio.findAll();
		Cliente clienteSelecionado = clienteSelecionador.selecionar(clientes, exclusao.getId());
		if (clienteSelecionado == null) {
			return new ResponseEntity<>(status);
		} else {
			List<Documento> documentos = repositorio.findAll();
			List<Documento> selecionados = new ArrayList<>();
			for (Documento documento : exclusao.getDocumentos()) {
				Documento selecionado = selecionador.selecionar(documentos, documento.getId());
				selecionados.add(selecionado);
			}
			clienteSelecionado.getDocumentos().removeAll(selecionados);
			clienteRepositorio.save(clienteSelecionado);
			repositorio.deleteAll(selecionados);
			
			status = HttpStatus.OK;
			return new ResponseEntity<>(status);
		}
	}
}
