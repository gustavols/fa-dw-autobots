package com.autobots.automanager.modelos.criadores;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.dtos.VeiculoDTO;
import com.autobots.automanager.dtos.VendaDTO;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.ConversorVeiculo;
import com.autobots.automanager.modelos.selecionadores.MercadoriaSelecionador;
import com.autobots.automanager.modelos.selecionadores.ServicoSelecionador;
import com.autobots.automanager.modelos.selecionadores.VeiculoSelecionador;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.VeiculoDTORepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

@Component
public class CriadorVenda {
	
	@Autowired
	ServicoRepositorio servicoRepositorio;
	@Autowired
	ServicoSelecionador servicoSelecionador;
	@Autowired
	MercadoriaRepositorio mercadoriaRepositorio;
	@Autowired
	MercadoriaSelecionador mercadoriaSelecionador;
	@Autowired
	VeiculoRepositorio veiculoRepositorio;
	@Autowired
	VeiculoSelecionador veiculoSelecionador;
	@Autowired
	ConversorVeiculo conversorVeiculo;
	@Autowired
	VeiculoDTORepositorio veiculoDTORepositorio;
	
	public Venda criar(VendaDTO vendaDTO) {
		
		Venda novaVenda = vendaDTO.getVenda();
		
		if (vendaDTO.getMercadoriasId() != null) {
			List<Mercadoria> mercadorias = mercadoriaRepositorio.findAll();
			for (Long id : vendaDTO.getMercadoriasId()) {
				Mercadoria mercadoria = mercadoriaSelecionador.selecionar(mercadorias, id);
				novaVenda.getMercadorias().add(mercadoria);
			}
		}
		
		if (vendaDTO.getServicosId() != null) {
			List<Servico> servicos = servicoRepositorio.findAll();
			for (Long id : vendaDTO.getServicosId()) {
				Servico servico = servicoSelecionador.selecionar(servicos, id);
				novaVenda.getServicos().add(servico);
			}
		}
		
		if (vendaDTO.getVeiculoId() != null) {
			VeiculoDTO veiculoDTO = veiculoDTORepositorio.getById(vendaDTO.getVeiculoId());
			novaVenda.setVeiculo(veiculoDTO);
		}
		
		novaVenda.setCadastro(new Date());
		
		return novaVenda;
	}
}
