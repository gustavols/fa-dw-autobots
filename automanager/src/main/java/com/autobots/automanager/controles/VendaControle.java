package com.autobots.automanager.controles;

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

import com.autobots.automanager.dtos.UsuarioVendaDTO;
import com.autobots.automanager.dtos.VendaDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.ConversorUsuarioVenda;
import com.autobots.automanager.modelos.criadores.CriadorVenda;
import com.autobots.automanager.modelos.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.modelos.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.modelos.selecionadores.UsuarioVendaDTOSelecionador;
import com.autobots.automanager.modelos.selecionadores.VendaSelecionador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.UsuarioVendaDTORepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@RestController
public class VendaControle {
	
	@Autowired
	VendaRepositorio repositorio;
	@Autowired
	VendaSelecionador selecionador;
	@Autowired
	CriadorVenda criador;
	@Autowired
	ConversorUsuarioVenda conversor;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@Autowired
	UsuarioSelecionador usuarioSelecionador;
	@Autowired
	EmpresaRepositorio empresaRepositorio;
	@Autowired
	EmpresaSelecionador empresaSelecionador;
	@Autowired
	UsuarioVendaDTORepositorio usuarioVendaDTORepositorio;
	@Autowired
	UsuarioVendaDTOSelecionador usuarioVendaDTOSelecionador;
	
	@GetMapping("/venda/{id}")
	public ResponseEntity<Venda> obterVenda(@PathVariable Long id){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Venda> vendas = repositorio.findAll();
		Venda selecionada = selecionador.selecionar(vendas, id);
		if (selecionada == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<Venda>(selecionada, status);
		}
	}
	
	@GetMapping("/vendas")
	public ResponseEntity<List<Venda>> obterVendas(){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Venda> vendas = repositorio.findAll();
		if (vendas == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Venda>>(vendas, status);
		}
	}
	
	@PutMapping("/venda/cadastrar/{empresaId}/{funcionarioId}/{clienteId}")
	public ResponseEntity<?> cadastrarVenda(@PathVariable Long empresaId, @PathVariable Long funcionarioId, @PathVariable Long clienteId, @RequestBody VendaDTO vendaDTO){
		HttpStatus status = HttpStatus.CONFLICT;
		List<Empresa> empresas = empresaRepositorio.findAll();
		List<UsuarioVendaDTO> usuariosVendaDTO = usuarioVendaDTORepositorio.findAll();
		Empresa empresa = empresaSelecionador.selecionar(empresas, empresaId);
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		Usuario cliente = usuarioSelecionador.selecionar(usuarios, clienteId);
		Usuario funcionario = usuarioSelecionador.selecionar(usuarios, funcionarioId);
		
		UsuarioVendaDTO clienteDTO = new UsuarioVendaDTO();
		UsuarioVendaDTO funcionarioDTO = new UsuarioVendaDTO();
		
		//cliente
		UsuarioVendaDTO verificacaoClienteDTO = usuarioVendaDTOSelecionador.selecionar(usuariosVendaDTO, clienteId);
		if (verificacaoClienteDTO == null) {
			conversor.converter(clienteDTO, cliente);
			usuarioVendaDTORepositorio.save(clienteDTO);
		} else {
			clienteDTO = verificacaoClienteDTO;
		}
		
		//funcionario
		UsuarioVendaDTO verificacaoFuncionarioDTO = usuarioVendaDTOSelecionador.selecionar(usuariosVendaDTO, funcionarioId);
		if (verificacaoFuncionarioDTO == null) {
			conversor.converter(funcionarioDTO, funcionario);
			usuarioVendaDTORepositorio.save(funcionarioDTO);
		} else {
			funcionarioDTO = verificacaoFuncionarioDTO;
		}
		
		if (vendaDTO.getVenda().getId() == null) {
			Venda novaVenda = criador.criar(vendaDTO);
			novaVenda.setCliente(clienteDTO);
			novaVenda.setFuncionario(funcionarioDTO);
			repositorio.save(novaVenda);
			
			cliente.getVendas().add(novaVenda);
			funcionario.getVendas().add(novaVenda);
			empresa.getVendas().add(novaVenda);
			
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/venda/deletar/{empresaId}/{vendaId}")
	public ResponseEntity<?> deletarVenda(@PathVariable Long empresaId, @PathVariable Long vendaId){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		List<Empresa> empresas = empresaRepositorio.findAll();
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		List<Venda> vendas = repositorio.findAll();
		
		Venda selecionada = selecionador.selecionar(vendas, vendaId);
		Empresa empresa = empresaSelecionador.selecionar(empresas, empresaId);
		
		if (selecionada != null && empresa != null) {
			Usuario cliente = usuarioSelecionador.selecionar(usuarios, selecionada.getCliente().getId());
			Usuario funcionario = usuarioSelecionador.selecionar(usuarios, selecionada.getFuncionario().getId());
			
			cliente.getVendas().remove(selecionada);
			funcionario.getVendas().remove(selecionada);
			empresa.getVendas().remove(selecionada);
			
			usuarioRepositorio.save(cliente);
			usuarioRepositorio.save(funcionario);
			empresaRepositorio.save(empresa);
			repositorio.delete(selecionada);
			
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<>(status);
	}
}
