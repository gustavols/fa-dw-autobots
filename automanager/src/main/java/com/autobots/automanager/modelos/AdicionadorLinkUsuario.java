package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entidades.Usuario;

public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario>{

	@Override
	public void adicionarLink(List<Usuario> lista) {
		for (Usuario cliente : lista) {
			long id = cliente.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(UsuarioControle.class)
							.obterUsuario(id))
					.withSelfRel();
			cliente.add(linkProprio);
		}
		
	}

	@Override
	public void adicionarLink(Usuario objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(UsuarioControle.class)
						.obterUsuarios())
				.withRel("usuarios");
		objeto.add(linkProprio);
		
	}

}
