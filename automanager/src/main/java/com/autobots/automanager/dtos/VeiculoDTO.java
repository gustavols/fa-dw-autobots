package com.autobots.automanager.dtos;


import javax.persistence.Entity;
import javax.persistence.Id;

import com.autobots.automanager.enumeracoes.TipoVeiculo;

import lombok.Data;

@Data
@Entity
public class VeiculoDTO {
	@Id
	private Long id;
	private TipoVeiculo tipo;
	private String modelo;
	private String placa;
}
