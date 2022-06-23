package com.autobots.automanager.modelos.selecionadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dtos.UsuarioVendaDTO;

@Component
public class UsuarioVendaDTOSelecionador {
	public UsuarioVendaDTO selecionar(List<UsuarioVendaDTO> usuariosVendaDTO, Long id) {
		UsuarioVendaDTO selecionado = null;
		for (UsuarioVendaDTO usuarioVendaDTO : usuariosVendaDTO) {
			if (usuarioVendaDTO.getId().equals(id)) {
				selecionado = usuarioVendaDTO;
			}
		}
		return selecionado;
	}
}
