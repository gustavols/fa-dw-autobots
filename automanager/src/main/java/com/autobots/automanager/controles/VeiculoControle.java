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

import com.autobots.automanager.dtos.VeiculoDTO;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.ConversorVeiculo;
import com.autobots.automanager.modelos.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.modelos.selecionadores.VeiculoSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoDTORepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

@RestController
public class VeiculoControle {
	
	@Autowired
	VeiculoRepositorio repositorio;
	@Autowired
	VeiculoSelecionador selecionador;
	@Autowired
	ConversorVeiculo conversor;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	@Autowired
	UsuarioSelecionador usuarioSelecionador;
	@Autowired
	VeiculoDTORepositorio veiculoDTORepositorio;
	
	@GetMapping("/veiculo/{id}")
	public ResponseEntity<Veiculo> obterVeiculo(@PathVariable Long id){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Veiculo> veiculos = repositorio.findAll();
		Veiculo selecionado = selecionador.selecionar(veiculos, id);
		if (selecionado == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<Veiculo>(selecionado, status);
		}
	}
	
	@GetMapping("/veiculos")
	public ResponseEntity<List<Veiculo>> obterVeiculos(){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Veiculo> veiculos = repositorio.findAll();
		if (veiculos == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Veiculo>>(veiculos, status);
		}
	}
	
	@PutMapping("/veiculo/cadastrar/{usuarioId}")
	public ResponseEntity<?> cadastrarVeiculo(@PathVariable Long usuarioId, @RequestBody Veiculo veiculo){
		HttpStatus status = HttpStatus.CONFLICT;
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		Usuario usuario = usuarioSelecionador.selecionar(usuarios, usuarioId);
		if (veiculo.getId() == null) {
			if (usuario == null) {
				status = HttpStatus.BAD_REQUEST;
			} else {
				VeiculoDTO veiculoDTO = new VeiculoDTO();
				veiculo.setProprietario(usuario);
				repositorio.save(veiculo);
				conversor.converter(veiculo, veiculoDTO);
				usuario.getVeiculos().add(veiculoDTO);
				usuarioRepositorio.save(usuario);
				veiculoDTORepositorio.save(veiculoDTO);
				status = HttpStatus.OK;
			}
		}
		
		return new ResponseEntity<>(status);
	}
	
	@DeleteMapping("/veiculo/deletar/{id}")
	public ResponseEntity<?> deletarVeiculo(@PathVariable Long id){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<VeiculoDTO> selecionados = new ArrayList<>();
		List<Veiculo> veiculos = repositorio.findAll();
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		Veiculo selecionado = selecionador.selecionar(veiculos, id);
		Usuario usuario = usuarioSelecionador.selecionar(usuarios, selecionado.getProprietario().getId());
		if (selecionado != null) {
			for (VeiculoDTO veiculo : usuario.getVeiculos()) {
				if (veiculo.getId().equals(id)) {
					selecionados.add(veiculo);
				}
			}
			usuario.getVeiculos().removeAll(selecionados);
			usuarioRepositorio.save(usuario);
			repositorio.delete(selecionado);
			veiculoDTORepositorio.delete(selecionados.get(0));
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<>(status);
	}
}
