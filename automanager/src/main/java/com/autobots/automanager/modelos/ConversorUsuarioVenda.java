package com.autobots.automanager.modelos;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dtos.UsuarioVendaDTO;
import com.autobots.automanager.entidades.Usuario;

@Component
public class ConversorUsuarioVenda {
	public void converter(UsuarioVendaDTO usuarioVendaDTO, Usuario usuario) {
		usuarioVendaDTO.setId(usuario.getId());
		usuarioVendaDTO.setNome(usuario.getNome());
		usuarioVendaDTO.setNomeSocial(usuario.getNomeSocial());
	}
}
