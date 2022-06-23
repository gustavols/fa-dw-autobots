package com.autobots.automanager.modelos;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dtos.VeiculoDTO;
import com.autobots.automanager.entidades.Veiculo;

@Component
public class ConversorVeiculo {
	public void converter(Veiculo veiculo, VeiculoDTO veiculoDTO) {
		
		veiculoDTO.setId(veiculo.getId());
		veiculoDTO.setModelo(veiculo.getModelo());
		veiculoDTO.setPlaca(veiculo.getPlaca());
		veiculoDTO.setTipo(veiculo.getTipo());
	}
}
