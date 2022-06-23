package com.autobots.automanager.modelos.criadores;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dtos.MercadoriaDTO;
import com.autobots.automanager.entidades.Mercadoria;

@Component
public class CriadorMercadoria {
	public Mercadoria criar(MercadoriaDTO mercadoriaDTO) {
		
		Mercadoria mercadoria = mercadoriaDTO.getMercadoria();
		mercadoria.setCadastro(new Date());
		mercadoria.setFabricao(new Date(mercadoriaDTO.getDataFabricacaotxt()));
		mercadoria.setValidade(new Date(mercadoriaDTO.getDataValidadetxt()));
		
		return mercadoria;
	}
}
