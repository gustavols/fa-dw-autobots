package com.autobots.automanager.modelos.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Servico;

@Component
public class ServicoSelecionador {
	public Servico selecionar(List<Servico> servicos, Long id) {
		Servico selecionado = null;
		for (Servico servico : servicos) {
			if (servico.getId().equals(id)) {
				selecionado = servico;
			}
		}
		return selecionado;
	}
}
