package com.autobots.automanager.dtos;

import com.autobots.automanager.entidades.Mercadoria;

import lombok.Data;

@Data
public class MercadoriaDTO {
	Mercadoria mercadoria;
	String razaoSocial;
	Long fornecedorId;
	
	String dataFabricacaotxt;
	String dataValidadetxt;
}
