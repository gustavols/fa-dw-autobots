package com.autobots.automanager.dtos;

import java.util.List;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Venda;

import lombok.Data;

@Data
public class VendaDTO {
	private Venda venda;
	private Long veiculoId;
	private List<Long> mercadoriasId;
	private List<Long> servicosId;
}
